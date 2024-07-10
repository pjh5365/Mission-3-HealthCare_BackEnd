package kakao.mission3healthcare_backend.auth.Controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberInfoRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberJoinRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberUpdateRequest;
import kakao.mission3healthcare_backend.auth.domain.dto.response.MemberInfoResponse;
import kakao.mission3healthcare_backend.auth.domain.dto.response.MemberResponse;
import kakao.mission3healthcare_backend.auth.service.MemberService;

/**
 * @author : parkjihyeok
 * @since : 2024/07/05
 */
@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class) // JPA 자동입력때문에 추가
@AutoConfigureRestDocs // REST Docs 추가
@DisplayName("회원 Controller 테스트")
class MemberControllerTest {

	@Autowired MockMvc mockMvc;
	@MockBean MemberService memberService;
	Gson gson = new Gson();

	@Test
	@DisplayName("회원가입 API 테스트")
	@WithMockUser
	void joinTest1() throws Exception {
	    // Given
		MemberJoinRequest request = new MemberJoinRequest("testId", "김철수", "1111");
		MemberResponse response = new MemberResponse(request.getUsername(), request.getName());
		given(memberService.join(any())).willReturn(response);

	    // When
		mockMvc.perform(post("/api/users")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("name").description("회원 이름"),
								fieldWithPath("password").description("비밀번호")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data.username").description("회원ID"),
								fieldWithPath("data.name").description("회원이름")
						)));

	    // Then
		verify(memberService).join(any());
	}

	@Test
	@DisplayName("회원가입 API 실패 테스트 (이미 존재하는 ID)")
	@WithMockUser
	void joinTest2() throws Exception {
		// Given
		MemberJoinRequest request = new MemberJoinRequest("testId", "김철수", "1111");
		given(memberService.join(any())).willThrow(new IllegalArgumentException("이미 존재하는 ID입니다."));

		// When
		mockMvc.perform(post("/api/users")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("name").description("회원 이름"),
								fieldWithPath("password").description("비밀번호")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(memberService).join(any());
	}

	@Test
	@DisplayName("회원정보수정 API 테스트 (이름 변경)")
	@WithMockUser
	void updateMemberNameTest1() throws Exception {
	    // Given
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "김철수");
		MemberResponse response = new MemberResponse(request.getUsername(), request.getName());
		given(memberService.updateMember(any())).willReturn(response);

	    // When
		mockMvc.perform(patch("/api/users")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("name").description("변경할 이름")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data.username").description("회원ID"),
								fieldWithPath("data.name").description("회원이름")
						)));

	    // Then
	}

	@Test
	@DisplayName("회원정보수정 API 실패 테스트 (이름 변경 / 회원정보를 찾을 수 없음)")
	@WithMockUser
	void updateMemberNameTest2() throws Exception {
		// Given
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "김철수");
		given(memberService.updateMember(any())).willThrow(new UsernameNotFoundException("회원가입 정보를 찾을 수 없습니다."));

		// When
		mockMvc.perform(patch("/api/users")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("name").description("변경할 이름")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
	}

	@Test
	@DisplayName("회원정보수정 API 테스트 (비밀번호 변경)")
	@WithMockUser
	void updateMemberPasswordTest1() throws Exception {
		// Given
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "1111", "2222");
		MemberResponse response = new MemberResponse(request.getUsername(), "김철수");
		given(memberService.updateMember(any())).willReturn(response);

		// When
		mockMvc.perform(patch("/api/users")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("beforePassword").description("기존 비밀번호"),
								fieldWithPath("newPassword").description("변경할 비밀번호")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data.username").description("회원ID"),
								fieldWithPath("data.name").description("회원이름")
						)));

		// Then
	}

	@Test
	@DisplayName("회원정보수정 API 실패 테스트 (비밀번호 변경 / 회원정보를 찾을 수 없음)")
	@WithMockUser
	void updateMemberPasswordTest2() throws Exception {
		// Given
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "1111", "2222");
		given(memberService.updateMember(any())).willThrow(new UsernameNotFoundException("회원가입 정보를 찾을 수 없습니다."));

		// When
		mockMvc.perform(patch("/api/users")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("beforePassword").description("기존 비밀번호"),
								fieldWithPath("newPassword").description("변경할 비밀번호")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
	}

	@Test
	@DisplayName("회원정보수정 API 실패 테스트 (비밀번호 변경 / 비밀번호가 일치하지 않음)")
	@WithMockUser
	void updateMemberPasswordTest3() throws Exception {
		// Given
		MemberUpdateRequest request = new MemberUpdateRequest("testId", "1111", "2222");
		given(memberService.updateMember(any())).willThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다!"));

		// When
		mockMvc.perform(patch("/api/users")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("beforePassword").description("기존 비밀번호"),
								fieldWithPath("newPassword").description("변경할 비밀번호")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
	}

	@Test
	@DisplayName("회원추가정보 입력 테스트")
	@WithMockUser
	void addMemberInfoTest1() throws Exception {
	    // Given
		MemberInfoRequest request = new MemberInfoRequest("testId", 70);
		MemberInfoResponse response = new MemberInfoResponse("testId", 70);
		given(memberService.addMemberInfo(any())).willReturn(response);

		// When
		mockMvc.perform(post("/api/users/details")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("goalWeight").description("목표 체중")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data.username").description("회원 ID"),
								fieldWithPath("data.goalWeight").description("목표 체중")
						)));

	    // Then
	}

	@Test
	@DisplayName("회원추가정보 입력 테스트 실패 (회원정보를 찾을 수 없음)")
	@WithMockUser
	void addMemberInfoTest2() throws Exception {
		// Given
		MemberInfoRequest request = new MemberInfoRequest("testId", 70);
		given(memberService.addMemberInfo(any())).willThrow(new UsernameNotFoundException("회원가입 정보를 찾을 수 없습니다."));

		// When
		mockMvc.perform(post("/api/users/details")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("goalWeight").description("목표 체중")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
	}
}
