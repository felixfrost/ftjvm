package com.Service.Service;

//import com.Model.Question;
import com.Model.QuizCategory;
import com.Service.ApplicationService;
import com.Service.HighScore;
import com.Service.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

@SpringBootTest
class ProjectApplicationTests {

	@Autowired
	ApplicationService sut;

/*	@Test
	void apiFetchAmountAndOrCategory() {
		Assertions.assertEquals(20, sut.getQuestions(20,"").size());
		List<Question> musicList = sut.getQuestions(30,"music");
		Assertions.assertEquals(30, musicList.size());

		musicList.forEach(q -> Assertions.assertEquals("Music", q.getCategory()));
	}*/

	@Test
	void categoryFancyStringsComparedToUrlStrings() {
		List<QuizCategory> categories = List.of(QuizCategory.values());
		categories.forEach(c -> Assertions.assertNotEquals(c.getUrlString(), c.getFancyString()));
	}

	@Test
	void fetchQuizLimits() {
		List<Integer> supposedValues = List.of(10,25,50);
		List<Integer> sutValues = sut.getQuizLimits();
		for (int i=0; i<sutValues.size(); i++) Assertions.assertEquals(supposedValues.get(i), sutValues.get(i));
	}

	@Test
	void fetchQuizCategories() {
		List<QuizCategory> supposedValues = List.of(QuizCategory.values());
		List<QuizCategory> sutValues = sut.getQuizCategories();
		for (int i=0; i<sutValues.size(); i++) Assertions.assertEquals(supposedValues.get(i), sutValues.get(i));

	}

	@Test
	void addThreeHighScoresAndCheckIfHighScoreListIsPaddedAndContainsTheAddedScores() {
		sut.saveScore(new HighScore(null, 9001, null, null));
		sut.saveScore(new HighScore(null, 9002, null, null));
		sut.saveScore(new HighScore(null, 9003, null, null));
		List<HighScore> sutValues = sut.getTopScores();

		Assertions.assertEquals(10, sutValues.size());
		for (int i=0; i<3; i++) Assertions.assertEquals(9003-i,sutValues.get(i).getScore());
	}

	@Test
	void addUserAndFindTheUserAndCheckIfIdWasGenerated() {
		User user = new User(null, "BingoBerra","UnCryptedPW","Bingo","Berra","Bingo@Lotto.se",1);
		sut.saveUser(user);

		Assertions.assertEquals(user.getEmail(),sut.findUser(user.getUsername()).getEmail());
		Assertions.assertNotNull(sut.findUser(user.getUsername()).getId());
	}
	

}
