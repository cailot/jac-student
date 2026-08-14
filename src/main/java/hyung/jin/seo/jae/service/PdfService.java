package hyung.jin.seo.jae.service;

import java.util.Map;

public interface PdfService {

	// generate empty test result pdf file
	byte[] generateEmptyTestResult(Long studentId);

	// generate test result pdf file
	byte[] generateTestResult(Map<String, Object> data);

	// generate assessment result pdf file
	byte[] generateAssessmentPdf(Map<String, Object> data);

}
