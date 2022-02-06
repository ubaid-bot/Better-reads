package com.betterreads.dataloader;

import com.betterreads.dataloader.connection.DataStaxAstraProperties;
import com.betterreads.dataloader.repository.AuthorRepository;
import com.betterreads.dataloader.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterReadsApplication {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Value("${datadump.location.author}")
    private String authorDump;

    @Value("${datadump.location.works}")
    private String worksDump;

    public static void main(String[] args) {
        SpringApplication.run(BetterReadsApplication.class, args);
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

 /*   private void inItAuthors() {
       *//* Path path = Paths.get(authorDump);
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
        }*//*
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
                    book.setId(jsonObject.optString("key").replace("/authors/", ""));
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
    }

    @PostConstruct
    private void start() {
     //   inItAuthors();
        inItBooks();
    }
*/

}
