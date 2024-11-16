package ru.urfu.sv.studentvoice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.sv.studentvoice.model.repository.ReviewRepository;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    private ReviewRepository repository;
    @Autowired
    private ClassSessionService sessionService;
    @Autowired
    private ReportService reportService;

//    @Transactional
//    public ActionResult saveReview(Review review) {
//        Optional<ClassSession> classSession = sessionService.findSessionById(review.getSessionId());
//
//        if (classSession.isEmpty()) {
//            String message = "Пара из отзыва %s не найдена".formatted(review);
//            log.warn(message);
//            throw new IllegalArgumentException(message);
//        }
//
//        Review saved = repository.save(review);
//        return repository.existsById(saved.getReviewId()) ? ActionResultFactory.reviewCreated(saved.toString()) : ActionResultFactory.reviewCreatingError();
//    }

//    public Float getAverageRatingBySessions(List<ClassSession> sessions) {
//        List<UUID> sessionsIds = sessions
//                .stream()
//                .map(ClassSession::getSessionId)
//                .toList();
//
//        if(sessionsIds.isEmpty()) return 0f;
//
//        List<Review> reviews = repository.findAllBySessionsIds(sessionsIds);
//
//        Integer sum = 0;
//        for (Review review : reviews) {
//            sum += review.getValue();
//        }
//
//        return reviews.isEmpty() ? 0f : (float) sum / reviews.size();
//    }

//    public List<Review> findReviewsBySessionId(UUID sessionId) {
//        return repository.findAllBySessionId(sessionId);
//    }
}