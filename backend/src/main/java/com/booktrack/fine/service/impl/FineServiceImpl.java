package com.booktrack.fine.service.impl;

import com.booktrack.circulation.entity.Circulation;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.fine.dto.request.CreateFineRequest;
import com.booktrack.fine.dto.request.PayFineRequest;
import com.booktrack.fine.dto.response.FineResponse;
import com.booktrack.fine.entity.Fine;
import com.booktrack.fine.enums.FineStatus;
import com.booktrack.fine.mapper.FineMapper;
import com.booktrack.fine.repository.FineRepository;
import com.booktrack.fine.service.FineService;
import com.booktrack.fine.validator.FineValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.booktrack.exception.ResourceNotFoundException;
import com.booktrack.notification.dto.request.CreateNotificationRequest;
import com.booktrack.notification.enums.NotificationType;
import com.booktrack.notification.service.NotificationService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class FineServiceImpl implements FineService {

        private final FineRepository fineRepository;

        private final FineMapper fineMapper;

        private final FineValidator fineValidator;

        private final PageResponseMapper pageResponseMapper;

        private final NotificationService notificationService;

        @Override
        public FineResponse createFine(
                        CreateFineRequest request) {

                Circulation circulation = fineValidator.validateCirculationExists(
                                request.getCirculationId());

                fineValidator.validateDuplicateFine(
                                request.getCirculationId());

                fineValidator.validateFineCreation(
                                circulation);

                Fine fine = Fine.builder()
                                .circulation(circulation)
                                .amount(request.getAmount())
                                .reason(request.getReason().trim())
                                .status(FineStatus.UNPAID)
                                .notes(request.getNotes())
                                .build();

                Fine savedFine = fineRepository.save(fine);

                notificationService.createNotification(
                                CreateNotificationRequest.builder()
                                                .userId(
                                                                circulation.getUser().getId())
                                                .type(
                                                                NotificationType.FINE_CREATED)
                                                .title("Fine Created")
                                                .message(
                                                                "A fine of ₹"
                                                                                + savedFine.getAmount()
                                                                                + " has been created for your account.")
                                                .build());

                return fineMapper.toResponse(savedFine);
        }

        @Override
        public FineResponse payFine(
                        PayFineRequest request) {

                Fine fine = fineValidator.validateFineExists(
                                request.getFineId());

                fineValidator.validatePayableFine(fine);

                fine.setStatus(
                                FineStatus.PAID);

                fine.setPaidAt(
                                LocalDateTime.now());

                if (request.getNotes() != null
                                && !request.getNotes().isBlank()) {

                        fine.setNotes(
                                        request.getNotes());
                }

                Fine updatedFine = fineRepository.save(fine);

                notificationService.createNotification(
                                CreateNotificationRequest.builder()
                                                .userId(
                                                                fine.getCirculation()
                                                                                .getUser()
                                                                                .getId())
                                                .type(
                                                                NotificationType.FINE_PAID)
                                                .title("Fine Paid")
                                                .message(
                                                                "Your fine of ₹"
                                                                                + updatedFine.getAmount()
                                                                                + " has been paid successfully.")
                                                .build());

                return fineMapper.toResponse(
                                updatedFine);
        }

        @Override
        @Transactional(readOnly = true)
        public FineResponse getFineById(
                        Long id) {

                Fine fine = fineValidator.validateFineExists(id);

                return fineMapper.toResponse(fine);
        }

        @Override
        @Transactional(readOnly = true)
        public FineResponse getFineByCirculation(
                        Long circulationId) {

                Fine fine = fineRepository.findByCirculationId(
                                circulationId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Fine not found for circulation: "
                                                                + circulationId));

                return fineMapper.toResponse(fine);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<FineResponse> getAllFines(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Fine> finePage = fineRepository.findAll(
                                pageable);

                return pageResponseMapper.toPageResponse(
                                finePage,
                                fineMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<FineResponse> getFinesByUser(
                        Long userId,
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Fine> finePage = fineRepository.findByCirculationUserId(
                                userId,
                                pageable);

                return pageResponseMapper.toPageResponse(
                                finePage,
                                fineMapper::toResponse);
        }
}