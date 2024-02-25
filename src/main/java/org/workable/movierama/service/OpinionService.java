package org.workable.movierama.service;

import org.workable.movierama.model.Movie;
import org.workable.movierama.model.User;

public interface OpinionService {

    void processOpinion(Movie movie, User user, String type);

}
