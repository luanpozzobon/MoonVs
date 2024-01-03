package luan.moonvs.controllers;

import luan.moonvs.models.requests.RateRequest;
import luan.moonvs.models.requests.RatingRequest;
import luan.moonvs.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    private RatingService service;

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
    public ResponseEntity<?> addOrEditRating(@PathVariable int idContent, @RequestBody RateRequest rateRequest) {
        if (idContent < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Content doesn't exist!");

        return service.addOrEditRating(idContent, rateRequest);
    }


    @GetMapping("/{idContent}")
    public ResponseEntity<?> getUserRating(@PathVariable int idContent) {
        if (idContent < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Conteúdo não existe");

        return service.getUserRating(idContent);
    }

    @GetMapping("/avg-rating/{idContent}")
    public ResponseEntity<?> getAvgRating(@PathVariable int idContent) {
        if (idContent < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Conteúdo não existe");

        return service.getAvgRating(idContent);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUserRatings() {
        return service.getAllUserRatings();
    }
}
