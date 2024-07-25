package kakao.mission3healthcare_backend.activity.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkDeleteRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.request.WalkUpdateRequest;
import kakao.mission3healthcare_backend.activity.domain.dto.response.WalkResponse;
import kakao.mission3healthcare_backend.activity.service.ActivityService;

/**
 * @author : parkjihyeok
 * @since : 2024/07/25
 */
@WebMvcTest(ActivityController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs // REST Docs 추가
@DisplayName("활동 Controller 테스트")
@WithMockUser
class ActivityControllerTest {

	@Autowired MockMvc mockMvc;
	@MockBean ActivityService activityService;

	/**
	 * Gson으로 LocalDate를 전송하기 위한 직렬화
	 */
	static class LocalDateTimeSerializer implements JsonSerializer<LocalDate> {
		private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		@Override
		public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
			return new JsonPrimitive(formatter.format(localDate));
		}
	}

	GsonBuilder gsonBuilder = new GsonBuilder();
	Gson gson;

	@BeforeEach
	void setUp() {
		gsonBuilder.registerTypeAdapter(LocalDate.class, new ActivityControllerTest.LocalDateTimeSerializer());
		gson = gsonBuilder.setPrettyPrinting().create();
	}

	@Test
	@DisplayName("한 회원의 걷기 정보를 반환하는 테스트")
	void getWalksTest() throws Exception {
		// Given
		WalkResponse r1 = new WalkResponse("testId", 1.1, 111.1, LocalDate.of(2024, 1, 1));
		WalkResponse r2 = new WalkResponse("testId", 2.1, 121.1, LocalDate.of(2024, 2, 1));
		WalkResponse r3 = new WalkResponse("testId", 3.1, 131.1, LocalDate.of(2024, 3, 1));
		WalkResponse r4 = new WalkResponse("testId", 4.1, 141.1, LocalDate.of(2024, 4, 1));
		WalkResponse r5 = new WalkResponse("testId", 5.1, 151.1, LocalDate.of(2024, 5, 1));

		given(activityService.findByUsername("testId")).willReturn(List.of(r1, r2, r3, r4, r5));

		// When
		mockMvc.perform(get("/api/activities/{username}", "testId"))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("username").description("회원ID")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data[].username").description("회원ID"),
								fieldWithPath("data[].distance").description("거리"),
								fieldWithPath("data[].avgHeartRate").description("평균 심박수"),
								fieldWithPath("data[].walkDate").description("날짜")
						)));

		// Then
		verify(activityService).findByUsername(any());
	}

	@Test
	@DisplayName("한 회원의 하루 걷기 정보를 반환하는 테스트")
	void getWalksByDateTest() throws Exception {
		// Given
		WalkResponse r1 = new WalkResponse("testId", 1.1, 111.1, LocalDate.of(2024, 1, 1));
		WalkResponse r2 = new WalkResponse("testId", 2.1, 121.1, LocalDate.of(2024, 1, 1));
		WalkResponse r3 = new WalkResponse("testId", 3.1, 131.1, LocalDate.of(2024, 1, 1));
		WalkResponse r4 = new WalkResponse("testId", 4.1, 141.1, LocalDate.of(2024, 1, 1));
		WalkResponse r5 = new WalkResponse("testId", 5.1, 151.1, LocalDate.of(2024, 1, 1));

		given(activityService.findByUsernameAndDate("testId", LocalDate.of(2024, 1, 1))).willReturn(List.of(r1, r2, r3, r4, r5));

		// When
		mockMvc.perform(get("/api/activities/{username}/{date}", "testId", LocalDate.of(2024, 1, 1)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("username").description("회원ID"),
								parameterWithName("date").description("조회할 날짜")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data[].username").description("회원ID"),
								fieldWithPath("data[].distance").description("거리"),
								fieldWithPath("data[].avgHeartRate").description("평균 심박수"),
								fieldWithPath("data[].walkDate").description("날짜")
						)));

		// Then
		verify(activityService).findByUsernameAndDate(any(), any());
	}

	@Test
	@DisplayName("하나의 걷기 정보를 반환하는 테스트 (성공)")
	void getWalkTest() throws Exception {
		// Given
		WalkResponse r = new WalkResponse("testId", 1.1, 111.1, LocalDate.of(2024, 1, 1));

		given(activityService.findById(100L)).willReturn(r);

		// When
		mockMvc.perform(get("/api/activities")
						.param("id", "100"))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						queryParameters(
								parameterWithName("id").description("걷기 정보 ID")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data.username").description("회원ID"),
								fieldWithPath("data.distance").description("거리"),
								fieldWithPath("data.avgHeartRate").description("평균 심박수"),
								fieldWithPath("data.walkDate").description("날짜")
						)));

		// Then
		verify(activityService).findById(any());
	}

	@Test
	@DisplayName("하나의 걷기 정보를 반환하는 테스트 (실패)")
	void getWalkFailTest() throws Exception {
		// Given
		given(activityService.findById(100L)).willThrow(new IllegalArgumentException("해당 활동(걷기)정보를 찾을 수 없습니다."));

		// When
		mockMvc.perform(get("/api/activities")
						.param("id", "100"))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						queryParameters(
								parameterWithName("id").description("걷기 정보 ID")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(activityService).findById(any());
	}

	@Test
	@DisplayName("걷기 정보 추가 테스트")
	void saveWalkTest() throws Exception{
		// Given
		WalkRequest request = new WalkRequest("testId", 3.1, 124.3, LocalDate.of(2024, 1, 1));

		// When
		mockMvc.perform(post("/api/activities")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원ID"),
								fieldWithPath("distance").description("거리"),
								fieldWithPath("avgHeartRate").description("평균 심박수"),
								fieldWithPath("walkDate").description("날짜")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("").ignored()
						)));

		// Then
		verify(activityService).saveWalk(any());
	}

	@Test
	@DisplayName("걷기 정보 추가 테스트 (실패)")
	void saveWalkFailTest() throws Exception {
		// Given
		WalkRequest request = new WalkRequest("testId", 3.1, 124.3, LocalDate.of(2024, 1, 1));
		doThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다.")).when(activityService).saveWalk(any());

		// When
		mockMvc.perform(post("/api/activities")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원ID"),
								fieldWithPath("distance").description("거리"),
								fieldWithPath("avgHeartRate").description("평균 심박수"),
								fieldWithPath("walkDate").description("날짜")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("").ignored()
						)));

		// Then
		verify(activityService).saveWalk(any());
	}

	@Test
	@DisplayName("걷기 정보 수정 테스트 (성공)")
	void updateWalkTest() throws Exception {
		// Given
		WalkUpdateRequest request = new WalkUpdateRequest(100L, "testId", 3.1, 124.3, LocalDate.of(2024, 1, 1));

		// When
		mockMvc.perform(patch("/api/activities")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("id").description("걷기 정보 ID"),
								fieldWithPath("username").description("회원ID"),
								fieldWithPath("distance").description("거리"),
								fieldWithPath("avgHeartRate").description("평균 심박수"),
								fieldWithPath("walkDate").description("날짜")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("").ignored()
						)));

		// Then
		verify(activityService).updateWalk(any());
	}

	@Test
	@DisplayName("걷기 정보 수정 테스트 (실패)")
	void updateWalkFailTest() throws Exception {
		// Given
		WalkUpdateRequest request = new WalkUpdateRequest(100L, "testId", 3.1, 124.3, LocalDate.of(2024, 1, 1));
		doThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다.")).when(activityService).updateWalk(any());

		// When
		mockMvc.perform(patch("/api/activities")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("id").description("걷기 정보 ID"),
								fieldWithPath("username").description("회원ID"),
								fieldWithPath("distance").description("거리"),
								fieldWithPath("avgHeartRate").description("평균 심박수"),
								fieldWithPath("walkDate").description("날짜")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("").ignored()
						)));

		// Then
		verify(activityService).updateWalk(any());
	}

	@Test
	@DisplayName("걷기 정보 삭제 테스트 (성공)")
	void deleteWalkTest() throws Exception {
		// Given
		WalkDeleteRequest request = new WalkDeleteRequest(100L, "testId");

		// When
		mockMvc.perform(delete("/api/activities")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("id").description("식단ID"),
								fieldWithPath("username").description("회원ID")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(activityService).deleteWalk(any());
	}

	@Test
	@DisplayName("걷기 정보 삭제 테스트 (실패)")
	void deleteWalkFailTest() throws Exception {
		// Given
		WalkDeleteRequest request = new WalkDeleteRequest(100L, "testId");
		doThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다.")).when(activityService).deleteWalk(any());

		// When
		mockMvc.perform(delete("/api/activities")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("id").description("식단ID"),
								fieldWithPath("username").description("회원ID")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(activityService).deleteWalk(any());
	}
}
