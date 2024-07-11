package kakao.mission3healthcare_backend.auth.Controller;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import kakao.mission3healthcare_backend.auth.domain.UserRole;
import kakao.mission3healthcare_backend.auth.domain.dto.request.MemberLoginRequest;
import kakao.mission3healthcare_backend.auth.domain.entity.Member;
import kakao.mission3healthcare_backend.auth.repository.MemberRepository;

/**
 * @author : parkjihyeok
 * @since : 2024/07/07
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayName("로그인 관련 테스트 코드")
public class MemberLoginTest {

	@Autowired MockMvc mockMvc;
	@Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired MemberRepository memberRepository;
	Gson gson = new Gson();

	@Test
	@DisplayName("로그인 성공 테스트")
	@WithMockUser
	@Transactional
	void loginTest1() throws Exception {
		// Given
		MemberLoginRequest request = new MemberLoginRequest("testId", "1111");
		Member member = new Member("testId", "김철수", bCryptPasswordEncoder.encode("1111"), UserRole.ROLE_USER);
		memberRepository.save(member);

		// When
		mockMvc.perform(post("/api/login")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().is3xxRedirection())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("password").description("비밀번호")
						)));

		// Then
	}

	@Test
	@DisplayName("로그인 실패 테스트(회원정보를 찾을 수 없음)")
	void loginTest2() throws Exception {
		// Given
		MemberLoginRequest request = new MemberLoginRequest("testId", "1111");

		// When
		mockMvc.perform(post("/api/login")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isUnauthorized())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("password").description("비밀번호")
						),
						responseFields(
								fieldWithPath("status").description("상태"),
								fieldWithPath("message").description("처리 결과에 대한 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
	}

	@Test
	@DisplayName("로그인 실패 테스트(비밀번호가 일치하지 않음)")
	@WithMockUser
	void loginTest3() throws Exception {
		// Given
		MemberLoginRequest request = new MemberLoginRequest("testId", "1111");
		Member member = new Member("testId", "김철수", bCryptPasswordEncoder.encode("2222"), UserRole.ROLE_USER);
		memberRepository.save(member);

		// When
		mockMvc.perform(post("/api/login")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isUnauthorized())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원 ID"),
								fieldWithPath("password").description("비밀번호")
						),
						responseFields(
								fieldWithPath("status").description("상태"),
								fieldWithPath("message").description("처리 결과에 대한 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
	}

	@Test
	@DisplayName("로그아웃 테스트")
	@WithMockUser
	void logOutTest() throws Exception {
		// Given

		// When
		mockMvc.perform(post("/api/logout")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andDo(print())
				.andDo((document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()))));

		// Then
	}

	@Test
	@DisplayName("권한이 없는 사용자가 접근시 리다이렉션 시키는지 확인하는 테스트")
	void unAuthorizationTest() throws Exception {
	    // Given

	    // When
		mockMvc.perform(post("/api/users/details")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andDo(print());

	    // Then
	}
}
