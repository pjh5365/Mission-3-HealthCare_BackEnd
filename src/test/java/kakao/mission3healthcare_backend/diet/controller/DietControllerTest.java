package kakao.mission3healthcare_backend.diet.controller;

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

import kakao.mission3healthcare_backend.diet.domain.MealType;
import kakao.mission3healthcare_backend.diet.domain.NutrientType;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietDeleteRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.DietUpdateRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.NutrientRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.request.SaveFoodRequest;
import kakao.mission3healthcare_backend.diet.domain.dto.response.DietResponse;
import kakao.mission3healthcare_backend.diet.domain.dto.response.FoodInfo;
import kakao.mission3healthcare_backend.diet.domain.dto.response.NutrientResponse;
import kakao.mission3healthcare_backend.diet.service.DietService;
import kakao.mission3healthcare_backend.diet.service.FoodMenuService;

/**
 * @author : parkjihyeok
 * @since : 2024/07/14
 */
@WebMvcTest(DietController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs // REST Docs 추가
@DisplayName("식단 Controller 테스트")
@WithMockUser
class DietControllerTest {

	@Autowired MockMvc mockMvc;
	@MockBean DietService dietService;
	@MockBean FoodMenuService foodMenuService;

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
		gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTimeSerializer());
		gson = gsonBuilder.setPrettyPrinting().create();
	}

	@Test
	@DisplayName("저장된 음식메뉴들 조회")
	void getFoodsTest() throws Exception {
	    // Given
		given(foodMenuService.getFood()).willReturn(List.of("햄버거", "감자튀김", "콜라", "치킨", "국밥", "김치"));

		// When
		mockMvc.perform(get("/api/foods"))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data.[]").description("음식 리스트")
						)));

		// Then
		verify(foodMenuService).getFood();
	}

	@Test
	@DisplayName("음식 메뉴 추가 테스트")
	void addFoodTest() throws Exception{
		// Given
		SaveFoodRequest request = new SaveFoodRequest("국밥", List.of(
				new NutrientRequest(NutrientType.PROTEIN, 20.1),
				new NutrientRequest(NutrientType.IRON, 2.1),
				new NutrientRequest(NutrientType.FAT, 11.1)
		));

		// When
		mockMvc.perform(post("/api/foods")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("foodName").description("추가할 음식이름"),
								fieldWithPath("nutrientRequests.[].nutrientType").description("영양소 구분"),
								fieldWithPath("nutrientRequests.[].amount").description("양")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("").ignored()
						)));

		// Then
		verify(foodMenuService).saveFoodMenu(any());
	}

	@Test
	@DisplayName("한 회원의 식단정보를 반환하는 테스트")
	void getDietsTest() throws Exception {
	    // Given
		NutrientResponse fi1f1nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 1.1);
		NutrientResponse fi1f1nr2 = new NutrientResponse(NutrientType.IRON, 1.2);
		NutrientResponse fi1f1nr3 = new NutrientResponse(NutrientType.PROTEIN, 1.3);
		NutrientResponse fi1f1nr4 = new NutrientResponse(NutrientType.FAT, 1.4);
		FoodInfo fi1f1 = new FoodInfo("국밥", List.of(fi1f1nr1, fi1f1nr2, fi1f1nr3, fi1f1nr4));

		NutrientResponse fi1f2nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 2.1);
		NutrientResponse fi1f2nr2 = new NutrientResponse(NutrientType.IRON, 2.2);
		NutrientResponse fi1f2nr3 = new NutrientResponse(NutrientType.PROTEIN, 2.3);
		NutrientResponse fi1f2nr4 = new NutrientResponse(NutrientType.FAT, 2.4);
		FoodInfo fi1f2 = new FoodInfo("콜라", List.of(fi1f2nr1, fi1f2nr2, fi1f2nr3, fi1f2nr4));

		NutrientResponse fi1f3nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 3.1);
		NutrientResponse fi1f3nr2 = new NutrientResponse(NutrientType.IRON, 3.2);
		NutrientResponse fi1f3nr3 = new NutrientResponse(NutrientType.PROTEIN, 3.3);
		NutrientResponse fi1f3nr4 = new NutrientResponse(NutrientType.FAT, 3.4);
		FoodInfo fi1f3 = new FoodInfo("김치", List.of(fi1f3nr1, fi1f3nr2, fi1f3nr3, fi1f3nr4));

		NutrientResponse fi2f1nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 10.1);
		NutrientResponse fi2f1nr2 = new NutrientResponse(NutrientType.IRON, 10.2);
		NutrientResponse fi2f1nr3 = new NutrientResponse(NutrientType.PROTEIN, 10.3);
		NutrientResponse fi2f1nr4 = new NutrientResponse(NutrientType.FAT, 10.4);
		FoodInfo fi2f1 = new FoodInfo("국밥", List.of(fi2f1nr1, fi2f1nr2, fi2f1nr3, fi2f1nr4));

		NutrientResponse fi2f2nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 20.1);
		NutrientResponse fi2f2nr2 = new NutrientResponse(NutrientType.IRON, 20.2);
		NutrientResponse fi2f2nr3 = new NutrientResponse(NutrientType.PROTEIN, 20.3);
		NutrientResponse fi2f2nr4 = new NutrientResponse(NutrientType.FAT, 20.4);
		FoodInfo fi2f2 = new FoodInfo("콜라", List.of(fi2f2nr1, fi2f2nr2, fi2f2nr3, fi2f2nr4));

		NutrientResponse fi2f3nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 30.1);
		NutrientResponse fi2f3nr2 = new NutrientResponse(NutrientType.IRON, 30.2);
		NutrientResponse fi2f3nr3 = new NutrientResponse(NutrientType.PROTEIN, 30.3);
		NutrientResponse fi2f3nr4 = new NutrientResponse(NutrientType.FAT, 30.4);
		FoodInfo fi2f3 = new FoodInfo("김치", List.of(fi2f3nr1, fi2f3nr2, fi2f3nr3, fi2f3nr4));

		NutrientResponse fi3f1nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 100.1);
		NutrientResponse fi3f1nr2 = new NutrientResponse(NutrientType.IRON, 100.2);
		NutrientResponse fi3f1nr3 = new NutrientResponse(NutrientType.PROTEIN, 100.3);
		NutrientResponse fi3f1nr4 = new NutrientResponse(NutrientType.FAT, 100.4);
		FoodInfo fi3f1 = new FoodInfo("국밥", List.of(fi3f1nr1, fi3f1nr2, fi3f1nr3, fi3f1nr4));

		NutrientResponse fi3f2nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 200.1);
		NutrientResponse fi3f2nr2 = new NutrientResponse(NutrientType.IRON, 200.2);
		NutrientResponse fi3f2nr3 = new NutrientResponse(NutrientType.PROTEIN, 200.3);
		NutrientResponse fi3f2nr4 = new NutrientResponse(NutrientType.FAT, 200.4);
		FoodInfo fi3f2 = new FoodInfo("콜라", List.of(fi3f2nr1, fi3f2nr2, fi3f2nr3, fi3f2nr4));

		NutrientResponse fi3f3nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 300.1);
		NutrientResponse fi3f3nr2 = new NutrientResponse(NutrientType.IRON, 300.2);
		NutrientResponse fi3f3nr3 = new NutrientResponse(NutrientType.PROTEIN, 300.3);
		NutrientResponse fi3f3nr4 = new NutrientResponse(NutrientType.FAT, 300.4);
		FoodInfo fi3f3 = new FoodInfo("김치", List.of(fi3f3nr1, fi3f3nr2, fi3f3nr3, fi3f3nr4));

		DietResponse dr1 = new DietResponse("testId", MealType.BREAKFAST, List.of(fi1f1, fi1f2, fi1f3),  LocalDate.of(2024, 1, 1));
		DietResponse dr2 = new DietResponse("testId", MealType.LUNCH, List.of(fi2f1, fi2f2, fi2f3),  LocalDate.of(2024, 1, 1));
		DietResponse dr3 = new DietResponse("testId", MealType.DINNER, List.of(fi3f1, fi3f2, fi3f3),  LocalDate.of(2024, 1, 1));

		given(dietService.getDiets("testId")).willReturn(List.of(dr1, dr2, dr3));

	    // When
		mockMvc.perform(get("/api/diets/{username}", "testId"))
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
								fieldWithPath("data[].mealType").description("식단 구분"),
								fieldWithPath("data[].dietDate").description("식단 날짜"),
								fieldWithPath("data[].foods[].foodName").description("음식 이름"),
								fieldWithPath("data[].foods[].nutrients").description("포함된 영양소들"),
								fieldWithPath("data[].foods[].nutrients[].nutrientType").description("영양소 구분"),
								fieldWithPath("data[].foods[].nutrients[].amount").description("영양소 양")
						)));

	    // Then
		verify(dietService).getDiets(any());
	}

	@Test
	@DisplayName("한 회원의 하루 식단정보를 반환하는 테스트")
	void getDietByUsernameAndDateTest() throws Exception {
		// Given
		NutrientResponse fi1f1nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 1.1);
		NutrientResponse fi1f1nr2 = new NutrientResponse(NutrientType.IRON, 1.2);
		NutrientResponse fi1f1nr3 = new NutrientResponse(NutrientType.PROTEIN, 1.3);
		NutrientResponse fi1f1nr4 = new NutrientResponse(NutrientType.FAT, 1.4);
		FoodInfo fi1f1 = new FoodInfo("국밥", List.of(fi1f1nr1, fi1f1nr2, fi1f1nr3, fi1f1nr4));

		NutrientResponse fi1f2nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 2.1);
		NutrientResponse fi1f2nr2 = new NutrientResponse(NutrientType.IRON, 2.2);
		NutrientResponse fi1f2nr3 = new NutrientResponse(NutrientType.PROTEIN, 2.3);
		NutrientResponse fi1f2nr4 = new NutrientResponse(NutrientType.FAT, 2.4);
		FoodInfo fi1f2 = new FoodInfo("콜라", List.of(fi1f2nr1, fi1f2nr2, fi1f2nr3, fi1f2nr4));

		NutrientResponse fi1f3nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 3.1);
		NutrientResponse fi1f3nr2 = new NutrientResponse(NutrientType.IRON, 3.2);
		NutrientResponse fi1f3nr3 = new NutrientResponse(NutrientType.PROTEIN, 3.3);
		NutrientResponse fi1f3nr4 = new NutrientResponse(NutrientType.FAT, 3.4);
		FoodInfo fi1f3 = new FoodInfo("김치", List.of(fi1f3nr1, fi1f3nr2, fi1f3nr3, fi1f3nr4));

		NutrientResponse fi2f1nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 10.1);
		NutrientResponse fi2f1nr2 = new NutrientResponse(NutrientType.IRON, 10.2);
		NutrientResponse fi2f1nr3 = new NutrientResponse(NutrientType.PROTEIN, 10.3);
		NutrientResponse fi2f1nr4 = new NutrientResponse(NutrientType.FAT, 10.4);
		FoodInfo fi2f1 = new FoodInfo("국밥", List.of(fi2f1nr1, fi2f1nr2, fi2f1nr3, fi2f1nr4));

		NutrientResponse fi2f2nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 20.1);
		NutrientResponse fi2f2nr2 = new NutrientResponse(NutrientType.IRON, 20.2);
		NutrientResponse fi2f2nr3 = new NutrientResponse(NutrientType.PROTEIN, 20.3);
		NutrientResponse fi2f2nr4 = new NutrientResponse(NutrientType.FAT, 20.4);
		FoodInfo fi2f2 = new FoodInfo("콜라", List.of(fi2f2nr1, fi2f2nr2, fi2f2nr3, fi2f2nr4));

		NutrientResponse fi2f3nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 30.1);
		NutrientResponse fi2f3nr2 = new NutrientResponse(NutrientType.IRON, 30.2);
		NutrientResponse fi2f3nr3 = new NutrientResponse(NutrientType.PROTEIN, 30.3);
		NutrientResponse fi2f3nr4 = new NutrientResponse(NutrientType.FAT, 30.4);
		FoodInfo fi2f3 = new FoodInfo("김치", List.of(fi2f3nr1, fi2f3nr2, fi2f3nr3, fi2f3nr4));

		NutrientResponse fi3f1nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 100.1);
		NutrientResponse fi3f1nr2 = new NutrientResponse(NutrientType.IRON, 100.2);
		NutrientResponse fi3f1nr3 = new NutrientResponse(NutrientType.PROTEIN, 100.3);
		NutrientResponse fi3f1nr4 = new NutrientResponse(NutrientType.FAT, 100.4);
		FoodInfo fi3f1 = new FoodInfo("국밥", List.of(fi3f1nr1, fi3f1nr2, fi3f1nr3, fi3f1nr4));

		NutrientResponse fi3f2nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 200.1);
		NutrientResponse fi3f2nr2 = new NutrientResponse(NutrientType.IRON, 200.2);
		NutrientResponse fi3f2nr3 = new NutrientResponse(NutrientType.PROTEIN, 200.3);
		NutrientResponse fi3f2nr4 = new NutrientResponse(NutrientType.FAT, 200.4);
		FoodInfo fi3f2 = new FoodInfo("콜라", List.of(fi3f2nr1, fi3f2nr2, fi3f2nr3, fi3f2nr4));

		NutrientResponse fi3f3nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 300.1);
		NutrientResponse fi3f3nr2 = new NutrientResponse(NutrientType.IRON, 300.2);
		NutrientResponse fi3f3nr3 = new NutrientResponse(NutrientType.PROTEIN, 300.3);
		NutrientResponse fi3f3nr4 = new NutrientResponse(NutrientType.FAT, 300.4);
		FoodInfo fi3f3 = new FoodInfo("김치", List.of(fi3f3nr1, fi3f3nr2, fi3f3nr3, fi3f3nr4));

		DietResponse dr1 = new DietResponse("testId", MealType.BREAKFAST, List.of(fi1f1, fi1f2, fi1f3),  LocalDate.of(2024, 1, 1));
		DietResponse dr2 = new DietResponse("testId", MealType.LUNCH, List.of(fi2f1, fi2f2, fi2f3),  LocalDate.of(2024, 1, 1));
		DietResponse dr3 = new DietResponse("testId", MealType.DINNER, List.of(fi3f1, fi3f2, fi3f3),  LocalDate.of(2024, 1, 1));

		given(dietService.getDietByUsernameAndDate("testId", LocalDate.of(2024, 1, 1))).willReturn(List.of(dr1, dr2, dr3));

		// When
		mockMvc.perform(get("/api/diets/{username}/{date}", "testId", "2024-01-01"))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("username").description("회원ID"),
								parameterWithName("date").description("검색할 날짜")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data[].username").description("회원ID"),
								fieldWithPath("data[].mealType").description("식단 구분"),
								fieldWithPath("data[].dietDate").description("식단 날짜"),
								fieldWithPath("data[].foods[].foodName").description("음식 이름"),
								fieldWithPath("data[].foods[].nutrients").description("포함된 영양소들"),
								fieldWithPath("data[].foods[].nutrients[].nutrientType").description("영양소 구분"),
								fieldWithPath("data[].foods[].nutrients[].amount").description("영양소 양")
						)));

		// Then
		verify(dietService).getDietByUsernameAndDate(any(), any());
	}

	@Test
	@DisplayName("하나의 식단정보를 반환하는 테스트 (성공)")
	void getDietByIdTest() throws Exception {
		// Given
		NutrientResponse fi1f1nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 1.1);
		NutrientResponse fi1f1nr2 = new NutrientResponse(NutrientType.IRON, 1.2);
		NutrientResponse fi1f1nr3 = new NutrientResponse(NutrientType.PROTEIN, 1.3);
		NutrientResponse fi1f1nr4 = new NutrientResponse(NutrientType.FAT, 1.4);
		FoodInfo fi1f1 = new FoodInfo("국밥", List.of(fi1f1nr1, fi1f1nr2, fi1f1nr3, fi1f1nr4));

		NutrientResponse fi1f2nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 2.1);
		NutrientResponse fi1f2nr2 = new NutrientResponse(NutrientType.IRON, 2.2);
		NutrientResponse fi1f2nr3 = new NutrientResponse(NutrientType.PROTEIN, 2.3);
		NutrientResponse fi1f2nr4 = new NutrientResponse(NutrientType.FAT, 2.4);
		FoodInfo fi1f2 = new FoodInfo("콜라", List.of(fi1f2nr1, fi1f2nr2, fi1f2nr3, fi1f2nr4));

		NutrientResponse fi1f3nr1 = new NutrientResponse(NutrientType.CARBOHYDRATES, 3.1);
		NutrientResponse fi1f3nr2 = new NutrientResponse(NutrientType.IRON, 3.2);
		NutrientResponse fi1f3nr3 = new NutrientResponse(NutrientType.PROTEIN, 3.3);
		NutrientResponse fi1f3nr4 = new NutrientResponse(NutrientType.FAT, 3.4);
		FoodInfo fi1f3 = new FoodInfo("김치", List.of(fi1f3nr1, fi1f3nr2, fi1f3nr3, fi1f3nr4));

		DietResponse dr1 = new DietResponse("testId", MealType.BREAKFAST, List.of(fi1f1, fi1f2, fi1f3),  LocalDate.of(2024, 1, 1));

		given(dietService.getDietById(100L)).willReturn(dr1);

		// When
		mockMvc.perform(get("/api/diets")
						.param("id", "100"))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						queryParameters(
								parameterWithName("id").description("식단 ID")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data.username").description("회원ID"),
								fieldWithPath("data.mealType").description("식단 구분"),
								fieldWithPath("data.dietDate").description("식단 날짜"),
								fieldWithPath("data.foods[].foodName").description("음식 이름"),
								fieldWithPath("data.foods[].nutrients").description("포함된 영양소들"),
								fieldWithPath("data.foods[].nutrients[].nutrientType").description("영양소 구분"),
								fieldWithPath("data.foods[].nutrients[].amount").description("영양소 양")
						)));

		// Then
		verify(dietService).getDietById(any());
	}

	@Test
	@DisplayName("하나의 식단정보를 반환하는 테스트 (실패)")
	void getDietByIdFailTest() throws Exception {
		// Given
		given(dietService.getDietById(100L)).willThrow(new IllegalArgumentException("해당 식단 정보를 찾을 수 없습니다."));

		// When
		mockMvc.perform(get("/api/diets")
						.param("id", "100"))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						queryParameters(
								parameterWithName("id").description("식단 ID")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(dietService).getDietById(any());
	}

	@Test
	@DisplayName("식단 추가 테스트 (성공)")
	void addDietTest() throws Exception {
		// Given
		DietRequest request = new DietRequest("testId", MealType.BREAKFAST, List.of("국밥", "김치", "콜라"), LocalDate.of(2024, 1, 1));

		// When
		mockMvc.perform(post("/api/diets")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원ID"),
								fieldWithPath("mealType").description("식단구분"),
								fieldWithPath("foodNames").description("음식 리스트"),
								fieldWithPath("dietDate").description("식단 날짜")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(dietService).addDiet(any());
	}

	@Test
	@DisplayName("식단 추가 테스트 (실패)")
	void addDietFailTest() throws Exception {
		// Given
		DietRequest request = new DietRequest("testId", MealType.BREAKFAST, List.of("국밥", "김치", "콜라"), LocalDate.of(2024, 1, 1));
		doThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다.")).when(dietService).addDiet(any());

		// When
		mockMvc.perform(post("/api/diets")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("username").description("회원ID"),
								fieldWithPath("mealType").description("식단구분"),
								fieldWithPath("foodNames").description("음식 리스트"),
								fieldWithPath("dietDate").description("식단 날짜")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(dietService).addDiet(any());
	}

	@Test
	@DisplayName("식단 수정 테스트 (성공)")
	void updateDietTest() throws Exception {
		// Given
		DietUpdateRequest request = new DietUpdateRequest(100L, "testId", MealType.BREAKFAST, List.of("국밥", "김치", "콜라"));

		// When
		mockMvc.perform(patch("/api/diets")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(request)))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("dietId").description("식단ID"),
								fieldWithPath("username").description("회원ID"),
								fieldWithPath("mealType").description("식단구분"),
								fieldWithPath("foodNames").description("음식 리스트")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(dietService).updateDiet(any());
	}

	@Test
	@DisplayName("식단 수정 테스트 (실패)")
	void updateDietFailTest() throws Exception {
		// Given
		DietUpdateRequest request = new DietUpdateRequest(100L, "testId", MealType.BREAKFAST, List.of("국밥", "김치", "콜라"));
		doThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다.")).when(dietService).updateDiet(any());

		// When
		mockMvc.perform(patch("/api/diets")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(request)))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(document("{class-name}/{method-name}/",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("dietId").description("식단ID"),
								fieldWithPath("username").description("회원ID"),
								fieldWithPath("mealType").description("식단구분"),
								fieldWithPath("foodNames").description("음식 리스트")
						),
						responseFields(
								fieldWithPath("status").description("처리 결과 상태"),
								fieldWithPath("message").description("처리 메시지"),
								fieldWithPath("data").description("처리 결과")
						)));

		// Then
		verify(dietService).updateDiet(any());
	}

	@Test
	@DisplayName("식단 삭제 테스트 (성공)")
	void deleteDietTest() throws Exception {
		// Given
		DietDeleteRequest request = new DietDeleteRequest(100L, "testId");

		// When
		mockMvc.perform(delete("/api/diets")
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
		verify(dietService).deleteDiet(any());
	}

	@Test
	@DisplayName("식단 삭제 테스트 (실패)")
	void deleteDietFailTest() throws Exception {
		// Given
		DietDeleteRequest request = new DietDeleteRequest(100L, "testId");
		doThrow(new UsernameNotFoundException("회원정보를 찾을 수 없습니다.")).when(dietService).deleteDiet(any());

		// When
		mockMvc.perform(delete("/api/diets")
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
		verify(dietService).deleteDiet(any());
	}
}
