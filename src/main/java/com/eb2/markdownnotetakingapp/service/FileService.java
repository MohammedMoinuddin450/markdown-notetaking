package com.eb2.markdownnotetakingapp.service;

import com.eb2.markdownnotetakingapp.dto.Position;
import com.eb2.markdownnotetakingapp.dto.Response;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class FileService {

    public List<Response> checkGrammar(MultipartFile file) {

        try {
            String contentAsString = file.getResource().getContentAsString(StandardCharsets.UTF_8);

            JLanguageTool jLanguageTool = new JLanguageTool(new AmericanEnglish());
            List<RuleMatch> check = jLanguageTool.check(contentAsString);

            return check.stream()
                    .filter(f -> !f.getSpecificRuleId().equals("WHITESPACE_RULE"))
                    .map(rm -> new Response(new Position(rm.getFromPos(), rm.getToPos()), rm.getMessage(), rm.getSuggestedReplacements())).toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertToHtml(MultipartFile file) {

        try {

            String contentAsString = file.getResource().getContentAsString(StandardCharsets.UTF_8);

            Parser parser = Parser.builder().build();
            Node doc = parser.parse(contentAsString);
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            return renderer.render(doc);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
