package ch.rasc.wampspring.demo.salmar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatsController {

	@RequestMapping("/stats")
	public @ResponseBody String showStats() {
		return "Stats";
	}

}
