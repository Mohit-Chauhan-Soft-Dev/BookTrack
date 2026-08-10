package com.booktrack.circulation.validator;

import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.bookcopy.enums.BookCopyStatus;
import com.booktrack.bookcopy.repository.BookCopyRepository;
import com.booktrack.circulation.entity.Circulation;
import com.booktrack.circulation.enums.CirculationStatus;
import com.booktrack.circulation.repository.CirculationRepository;
import com.booktrack.exception.BadRequestException;
import com.booktrack.exception.ResourceNotFoundException;
import com.booktrack.user.entity.User;
import com.booktrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CirculationValidator {

        private final CirculationRepository circulationRepository;
        private final BookCopyRepository bookCopyRepository;
        private final UserRepository userRepository;

        public BookCopy validateBookCopyExists(Long bookCopyId) {

                return bookCopyRepository.findById(bookCopyId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Book copy not found with id: " + bookCopyId));
        }

        public User validateUserExists(Long userId) {

                return userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found with id: " + userId));
        }

        public Circulation validateCirculationExists(Long circulationId) {

                return circulationRepository.findById(circulationId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Circulation not found with id: " + circulationId));
        }

        public void validateBorrowableBookCopy(BookCopy bookCopy) {

                if (!bookCopy.isActive()) {
                        throw new BadRequestException(
                                        "Cannot borrow an inactive book copy.");
                }

                if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {
                        throw new BadRequestException(
                                        "Book copy is not available for borrowing.");
                }

                boolean alreadyBorrowed = circulationRepository.existsByBookCopyIdAndStatus(
                                bookCopy.getId(),
                                CirculationStatus.ACTIVE);

                if (alreadyBorrowed) {
                        throw new BadRequestException(
                                        "Book copy already has an active circulation.");
                }
        }

        public void validateReturnableCirculation(
                        Circulation circulation) {

                if (circulation.getStatus() != CirculationStatus.ACTIVE
                                && circulation.getStatus() != CirculationStatus.OVERDUE) {

                        throw new BadRequestException(
                                        "Circulation cannot be returned.");
                }

                if (circulation.getBookCopy().getStatus() != BookCopyStatus.BORROWED) {

                        throw new BadRequestException(
                                        "Book copy is not currently borrowed.");
                }
        }
}