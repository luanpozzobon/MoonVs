package luan.moonvs.controllers;

import luan.moonvs.models.entities.Rating;
import luan.moonvs.models.requests.RatingRequest;
import luan.moonvs.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    private RatingService service;

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

    @GetMapping("/{idContent}")
    public ResponseEntity<?> getRating(@PathVariable int idContent) {
        if (idContent < 1)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Conteúdo não existe");

        return service.getRating(idContent);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUserRatings() {
        return service.getAllUserRatings();
    }
}
