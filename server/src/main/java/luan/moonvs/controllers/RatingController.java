package luan.moonvs.controllers;

import luan.moonvs.models.entities.Rating;
import luan.moonvs.models.requests.RateRequest;
import luan.moonvs.models.requests.RatingRequest;
import luan.moonvs.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    private RatingService service;

    private final String HEADER_NAME = "message";
    private final String NOT_EXISTS = "The content doesn't exist";

    @Deprecated
    @PostMapping("/rate")
    public ResponseEntity<?> newRating(@RequestBody RatingRequest rating) {
        if (rating.idContent() < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Conteúdo não existe!");

        if (rating.ratingValue() < 0.0 || rating.ratingValue() > 10.0)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("A avaliação deve estar entre 0.0 e 10.0");

        return service.newRating(rating);
    }

    @PostMapping("/rate/{idContent}")
    public ResponseEntity<Rating> addOrEditRating(@PathVariable int idContent, @RequestBody RateRequest rateRequest) {
        if (idContent < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, NOT_EXISTS)
                    .build();

        var response = service.addOrEditRating(idContent, rateRequest);
        return ResponseEntity
                .status(response.status())
                .header(response.message())
                .body(response.entity());
    }


    @GetMapping("/{idContent}")
    public ResponseEntity<Rating> getUserRating(@PathVariable int idContent) {
        if (idContent < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, NOT_EXISTS)
                    .build();

        var response = service.getUserRating(idContent);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }

    @GetMapping("/avg-rating/{idContent}")
    public ResponseEntity<Float> getAvgRating(@PathVariable int idContent) {
        if (idContent < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, NOT_EXISTS)
                    .build();

        var response = service.getAvgRating(idContent);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }

    @Deprecated
    @GetMapping("/")
    public ResponseEntity<?> getAllUserRatings() {
        return service.getAllUserRatings();
    }

    @GetMapping("/list/{idUser}")
    public ResponseEntity<List<Rating>> getUserRatingList(@PathVariable UUID idUser) {
        final String MISSING_ID = "It is necessary the Id of the user!";

        if (idUser == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, MISSING_ID)
                    .build();

        var response = service.getUserRatingList(idUser);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }
}
