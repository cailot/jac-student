package hyung.jin.seo.jae.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OnlineControllerTest {

	@Test
	void jmssAcademicSet21MapsToSet1() {
		assertEquals(1, OnlineController.applyRecordedLessonSetOffset("JMSS", 21));
	}

	@Test
	void jmssSetBeforeFirstReleaseIsInvalid() {
		assertEquals(0, OnlineController.applyRecordedLessonSetOffset("JMSS", 20));
	}

	@Test
	void nonJmssGradesKeepExistingSetNumber() {
		String[] unaffectedGrades = {
				"P2", "P3", "P4", "P5", "P6", "S7", "S8", "S9", "S10",
				"FT6", "FT8", "TT6", "TT8"
		};

		for (String grade : unaffectedGrades) {
			assertEquals(21, OnlineController.applyRecordedLessonSetOffset(grade, 21), grade);
		}
	}
}
