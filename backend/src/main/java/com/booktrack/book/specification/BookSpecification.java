package com.booktrack.book.specification;

import com.booktrack.book.entity.Book;
import com.booktrack.book.enums.BookStatus;
import com.booktrack.book.enums.Language;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecification {

    private BookSpecification() {
    }

    public static Specification<Book> hasTitle(String title) {

        return (root, query, cb) ->

                title == null || title.isBlank()

                        ? null

                        : cb.like(
                                cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"
                        );
    }

    public static Specification<Book> hasCategory(Long categoryId) {

        return (root, query, cb) ->

                categoryId == null

                        ? null

                        : cb.equal(
                                root.get("category").get("id"),
                                categoryId
                        );
    }

    public static Specification<Book> hasPublisher(Long publisherId) {

        return (root, query, cb) ->

                publisherId == null

                        ? null

                        : cb.equal(
                                root.get("publisher").get("id"),
                                publisherId
                        );
    }

    public static Specification<Book> hasLanguage(Language language) {

        return (root, query, cb) ->

                language == null

                        ? null

                        : cb.equal(
                                root.get("language"),
                                language
                        );
    }

    public static Specification<Book> hasStatus(BookStatus status) {

        return (root, query, cb) ->

                status == null

                        ? null

                        : cb.equal(
                                root.get("status"),
                                status
                        );
    }

    public static Specification<Book> isActive(Boolean active) {

        return (root, query, cb) ->

                active == null

                        ? null

                        : cb.equal(
                                root.get("active"),
                                active
                        );
    }

}