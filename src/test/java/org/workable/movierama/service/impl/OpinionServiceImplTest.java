package org.workable.movierama.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workable.movierama.enumeration.OpinionType;
import org.workable.movierama.model.Movie;
import org.workable.movierama.model.Opinion;
import org.workable.movierama.model.User;
import org.workable.movierama.repository.OpinionRepository;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpinionServiceImplTest {

    @Mock
    private OpinionRepository opinionRepository;

    @InjectMocks
    private OpinionServiceImpl opinionService;

    private Movie movie;
    private User user;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        user = new User();
    }

    @Test
    void testProcessOpinion_NewOpinion() {
        when(opinionRepository.save(any(Opinion.class))).thenReturn(new Opinion());

        // Assume no existing opinions for simplicity
        movie.setOpinions(new HashSet<>());

        opinionService.processOpinion(movie, user, OpinionType.LIKE.getType());

        verify(opinionRepository, times(1)).save(any(Opinion.class));
    }

    @Test
    void testProcessOpinion_RetractOpinion() {
        Opinion existingOpinion = new Opinion(); // Setup existing opinion
        existingOpinion.setOpinionType(OpinionType.LIKE);
        existingOpinion.setUser(user);
        movie.setOpinions(new HashSet<>(List.of(existingOpinion)));

        doNothing().when(opinionRepository).delete(any(Opinion.class));

        opinionService.processOpinion(movie, user, OpinionType.LIKE.getType());

        verify(opinionRepository, times(1)).delete(any(Opinion.class));
    }

    @Test
    void testProcessOpinion_SwitchOpinion() {
        Opinion existingOpinion = new Opinion(); // Setup existing opinion
        existingOpinion.setOpinionType(OpinionType.LIKE);
        existingOpinion.setUser(user);
        movie.setOpinions(new HashSet<>(List.of(existingOpinion)));

        when(opinionRepository.save(any(Opinion.class))).thenReturn(new Opinion());

        opinionService.processOpinion(movie, user, OpinionType.HATE.getType());

        verify(opinionRepository, times(1)).save(any(Opinion.class));
    }
}
