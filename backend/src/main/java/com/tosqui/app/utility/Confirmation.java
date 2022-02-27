package com.tosqui.app.utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Confirmation {

	public static void show(String username, String verificationToken) throws IOException, URISyntaxException {

		String fileName = "/home/tosqui/Scrivania/confirmation.html";
		List<String> newLines = new ArrayList<>();
		for (String line : Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8)) {
			if (line.contains("XXXXX")) {
				newLines.add(line.replace("XXXXX", "http://localhost:4200/verify/?username=" + username
						+ "&&verificationToken=" + verificationToken));
			} else {
				newLines.add(line);
			}
		}
		Files.write(Paths.get("/tmp/demo1.html"), newLines, StandardCharsets.UTF_8);
		String[] params2 = new String[] { "/bin/sh", "-c", "firefox /tmp/demo1.html" };
		new ProcessBuilder(params2).start();
	}
}
