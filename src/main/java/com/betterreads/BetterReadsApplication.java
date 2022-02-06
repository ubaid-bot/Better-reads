package com.betterreads;

import com.betterreads.connection.DataStaxAstraProperties;
import com.betterreads.model.Author;
import com.betterreads.model.Book;
import com.betterreads.repository.AuthorRepository;
import com.betterreads.repository.BookRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
@RestController
public class BetterReadsApplication {

   /* @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Value("${datadump.location.author}")
    private String authorDump;

    @Value("${datadump.location.works}")
    private String worksDump;*/


    public static void main(String[] args) {
        SpringApplication.run(BetterReadsApplication.class, args);
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }


    @RequestMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principle){
          System.out.println(principle);
          return principle.getAttribute("name");
    }

    /*private void inItAuthors() {
        Path path = Paths.get(authorDump);
        try {
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> {
                String jsonString = line.substring(line.indexOf("{"));
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Author author = new Author();
                    author.setId(jsonObject.optString("key").replace("/authors/", ""));
                    author.setName(jsonObject.optString("name"));
                    author.setPersonalName(jsonObject.optString("personal_name"));
                    System.out.println("saving.........."+author.getName());
                    authorRepository.save(author);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inItBooks() {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        Path path = Paths.get(worksDump);
        try {
            Stream<String> lines = Files.lines(path);
            lines.limit(50).forEach(line -> {
                String jsonString = line.substring(line.indexOf("{"));
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Book book = new Book();
                    book.setId(jsonObject.getString("key").replace("/works/", ""));
                    book.setName(jsonObject.optString("title"));
                    JSONObject description = jsonObject.optJSONObject("description");
                    if (description != null) {
                        book.setDescription(description.optString("value"));
                    }
                    JSONObject publishObj = jsonObject.getJSONObject("created");
                    if (publishObj != null) {
                        book.setPublishedDate(LocalDate.parse(publishObj.getString("value"),formatter));
                    }
                    JSONArray jsonArray = jsonObject.optJSONArray("covers");
                    if (jsonArray != null) {
                        List<String> coverIds = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            coverIds.add(jsonArray.getString(i));
                        }
                        book.setCoverIds(coverIds);
                    }

                    JSONArray authJsonArray = jsonObject.optJSONArray("authors");
                    if (jsonArray != null) {
                        List<String> authIds = new ArrayList<>();
                        for (int i = 0; i < authJsonArray.length(); i++) {
                            String id = authJsonArray.getJSONObject(i).getJSONObject("author").getString("key")
                                    .replace("/authors/", "");
                            authIds.add(id);
                        }
                        book.setAuthorIds(authIds);
                        List<String> authNames = authIds.stream().map(id -> authorRepository.findById(id))
                                .map(opt -> {
                                    if (!opt.isPresent()) return "Unknown Author";
                                    return opt.get().getName();
                                }).collect(Collectors.toList());
                        book.setAuthorNames(authNames);
                    }

                    bookRepository.save(book);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

  /*  @PostConstruct
    private void start() {
     //   inItAuthors();
        inItBooks();
    }*/

}
