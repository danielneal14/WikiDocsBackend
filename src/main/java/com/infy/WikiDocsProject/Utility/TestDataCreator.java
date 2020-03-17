package com.infy.WikiDocsProject.Utility;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.text.TextProducer;
import com.infy.WikiDocsProject.Model.Article;
import com.infy.WikiDocsProject.Model.User;
import com.infy.WikiDocsProject.enums.Role;
import com.infy.WikiDocsProject.enums.Status;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestDataCreator {

    private static final Fairy fairy = Fairy.create();
    private static final BaseProducer baseProducer = fairy.baseProducer();
    private static final TextProducer textProducer = fairy.textProducer();

//    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * Creates 5 users using jFairy.
     * @return the list of users generated
     */
    public static List<User> createUsers() {
        List<User> users = new ArrayList<>();
        Person person;

        for (int i = 0; i <= 5; i++) {
            person = fairy.person();

            List<Article> articles = createArticles(person.getEmail());
            User user = User.builder()
                    .id(new ObjectId())
                    .email(person.getEmail())
                    .name(person.getFullName())
//                    .password(bCryptPasswordEncoder.encode(person.getPassword()))
                    .password(person.getPassword())
                    .role(Role.USER)
                    .articles(articles)
                    .build();
            users.add(user);
        }
        return users;
    }

    /**
     * Creates a list of optional users.
     * Calls createUsers to create users and
     * uses a lambda expression to
     * convert them into optionals
     * @return The list of optional users generated
     */
    public static List<Optional<User>> createOptionalUsers(){
        List<Optional<User>> optionals = new ArrayList<>();
        List<User> users = createUsers();
        users.forEach(user -> optionals.add(Optional.of(user)));
        return optionals;
    }

    /**
     * Creates an Article with given email and status
     * @param email provided email to set article's emailId
     * @param status provided status to set article's status
     * @return generated article
     */
    public static Article createArticleByUserWithStatus(String email, Status status) {
        Article article = Article.builder()
                .id(new ObjectId())
                .emailId(email)
                .name(textProducer.sentence(1))
                .content(textProducer.paragraph(1))
                .status(status)
                .rejectedCount(baseProducer.randomBetween(0,2))
                .build();
        switch(status){
            case APPROVED:
            case BETA:
                article.setReadOnly(true);
                break;
            case DISCARDED:
                article.setReadOnly(true);
                article.setRejectedCount(4);
                break;
            case REJECTED:
                article.setReadOnly(false);
                break;
            case INITIAL:
                article.setReadOnly(false);
                article.setRejectedCount(0);
                break;
        }
        return article;
    }

    /**
     * Creates 5 articles reflecting the 5 Status enums
     * with a given users email for the article's emailId
     * @param email provided email to pass to createArticleByUserWithStatus()
     * @return the list of articles generated
     */
    public static List<Article> createArticles(String email) {
        List<Article> articles = new ArrayList<>();
        articles.add(createArticleByUserWithStatus(email, Status.APPROVED));
        articles.add(createArticleByUserWithStatus(email, Status.BETA));
        articles.add(createArticleByUserWithStatus(email, Status.INITIAL));
        articles.add(createArticleByUserWithStatus(email, Status.REJECTED));
        articles.add(createArticleByUserWithStatus(email, Status.DISCARDED));
        return articles;
    }

    /**
     * Creates a list of optional articles.
     * Calls createArticles to create articles.
     * Uses lambda expression to convert them to optionals.
     * @return List of optional articles
     */
    public static List<Optional<Article>> createOptionalArticles(){
        List<Optional<Article>> optionals = new ArrayList<>();
        List<Article> articles = createArticles("john@gmail.com");
        articles.forEach(article -> optionals.add(Optional.of(article)));
        return optionals;
    }
}