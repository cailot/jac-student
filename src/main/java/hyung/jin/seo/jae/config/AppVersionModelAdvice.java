package hyung.jin.seo.jae.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Exposes application.properties values to all views as model attributes.
 */
@ControllerAdvice
public class AppVersionModelAdvice {

	@Value("${app.version:unknown}")
	private String appVersion;

	@Value("${mock.explanation.enabled:false}")
	private boolean mockExplanationEnabled;

	@ModelAttribute("appVersion")
	public String appVersion() {
		return appVersion;
	}

	@ModelAttribute("mockExplanationEnabled")
	public boolean mockExplanationEnabled() {
		return mockExplanationEnabled;
	}
}
