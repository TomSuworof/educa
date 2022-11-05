package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.repositories.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public void saveExercise(Exercise exercise) {
        exerciseRepository.save(exercise);

//        switch (exercise.getState()) {
//            case PUBLISHED -> publish(exercise);
//            case IN_EDITING -> saveDraft(exercise);
//            default -> throw new IllegalStateException();
//        }
    }

//    private void saveDraft(Exercise newArticle) {
//        Optional<Article> oldArticleOpt = exerciseRepository.findById(newArticle.getId());
//
//        if (oldArticleOpt.isEmpty()) {
//            // completely new draft
//            // and article with URL exists
//            if (exerciseRepository.findByUrl(newArticle.getUrl()).isPresent()) {
//                throw new DuplicatedArticleException();
//            }
//        }
//
//        if (oldArticleOpt.isPresent()) {
//            Article oldArticle = oldArticleOpt.get();
//
//            // prevent from changing someone's article
//            if (!newArticle.getAuthor().equals(oldArticle.getAuthor())) {
//                throw new ArticleNotFoundException();
//            }
//
//            exerciseRepository.delete(oldArticle);
//            articleSearchRepository.delete(oldArticle);
//        }
//
//        exerciseRepository.save(newArticle);
//        articleSearchRepository.save(newArticle);
//    }
//
//    private void publish(Article newArticle) {
//        Optional<Article> oldArticleOfSameAuthor = exerciseRepository.findArticleByTitleAndStateAndAuthor_Username(
//                newArticle.getTitle(),
//                newArticle.getState(),
//                newArticle.getAuthor().getUsername());
//
//        if (oldArticleOfSameAuthor.isPresent()) {
//            Optional<Article> oldArticleWithSameUrl = exerciseRepository.findByUrl(newArticle.getUrl());
//
//            if (oldArticleWithSameUrl.isPresent()) {
//                Article oldArticle = oldArticleWithSameUrl.get();
//
//                String oldArticleUsername = oldArticle.getAuthor().getUsername();
//                String newArticleUsername = newArticle.getAuthor().getUsername();
//
//                if (!oldArticleUsername.equals(newArticleUsername)) {
//                    // title, state, url are the same, but different authors - error
//                    throw new DuplicatedArticleException();
//                }
//            }
//
//            // title, state, url, author are the same - just update
//            Article oldArticle = oldArticleOfSameAuthor.get();
//            newArticle.setPublicationDate(oldArticle.getPublicationDate());
//
//            newArticle.setId(oldArticle.getId());
//
//            exerciseRepository.delete(oldArticle);
//            articleSearchRepository.delete(oldArticle);
//
//            exerciseRepository.save(newArticle);
//            articleSearchRepository.save(newArticle);
//
//            authorService.notifyAboutNewArticle(newArticle);
//
//            return;
//        }
//
//        // same url, but different titles, states or authors - error
//        if (exerciseRepository.findByUrl(newArticle.getUrl()).isPresent()) {
//            throw new DuplicatedArticleException();
//        }
//
//        exerciseRepository.save(newArticle);
//        articleSearchRepository.save(newArticle);
//
//        authorService.notifyAboutNewArticle(newArticle);
//    }

    public void deleteExerciseById(UUID id) {
        if (exerciseRepository.findById(id).isPresent()) {
            exerciseRepository.deleteById(id);
        } else {
            throw new IllegalStateException();
        }
    }

    public Exercise getExerciseById(UUID id) {
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(id);
        if (exerciseOpt.isPresent()) {
            return exerciseOpt.get();
        } else {
            throw new IllegalStateException();
        }
    }

//
//    public ArticleDTO getNextArticle(String url) {
//        return getArticleWithOffset(url, 1);
//    }
//
//    public ArticleDTO getPreviousArticle(String url) {
//        return getArticleWithOffset(url, -1);
//    }
//
//    private ArticleDTO getArticleWithOffset(String url, int offset) {
//        Article article = getArticleByUrl(url);
//        List<Article> articles = exerciseRepository.findArticlesByStateAndAuthor_Username(ArticleState.PUBLISHED, article.getAuthor().getUsername());
//        articles.sort(Comparator.comparing(Article::getPublicationDate));
//
//        int articleIndex = articles.indexOf(article);
//
//        try {
//            return new ArticleDTO(articles.get(articleIndex + offset));
//        } catch (IndexOutOfBoundsException e) {
//            throw new ArticleNotFoundException();
//        }
//    }


//    public List<ArticleDTO> getSuggestedArticles(String url) {
//        Article mainArticle = getArticleByUrl(url);
//        Set<String> mainTags = mainArticle.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
//
//        Set<ArticleDTO> suggestedArticles = new LinkedHashSet<>();
//
//        for (String tag : mainTags) {
//            suggestedArticles.addAll(tagService.getPageWithArticles(tag, 10, 0).getEntities());
//        }
//        suggestedArticles.remove(new ArticleDTO(mainArticle));
//
//        Map<ArticleDTO, Integer> rating = new HashMap<>();
//
//        for (ArticleDTO articleDTO : suggestedArticles) {
//            Set<String> tags = new HashSet<>(articleDTO.getTags());
//            tags.retainAll(mainTags);
//            rating.put(articleDTO, tags.size());
//        }
//
//        return rating
//                .entrySet()
//                .stream()
//                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//                .map(Map.Entry::getKey)
//                .limit(3) // make a parameter
//                .toList();
//    }


//    public PageResponseDTO<ArticleDTO> getPageWithArticlesByAuthorAndStatePaginated(User author, ArticleState state, Integer limit, Integer offset) {
//        Page<Article> articles;
//
//        if (state.equals(ArticleState.ALL)) {
//            articles = getAllArticlesByAuthorPaginated(author, limit, offset);
//        } else {
//            articles = getArticlesByAuthorAndStatePaginated(author, state, limit, offset);
//        }
//
//        return new PageResponseDTO<>(
//                offset > 0 && articles.getTotalElements() > 0,
//                (offset + limit) < articles.getTotalElements(),
//                articles.getContent().stream().map(ArticleDTO::new).toList(),
//                articles.getTotalElements());
//    }
//
//    private Page<Article> getAllArticlesByAuthorPaginated(User author, Integer limit, Integer offset) {
//        return exerciseRepository.findArticlesByAuthor(author, getDefaultPageable(limit, offset));
//    }

//    private Page<Article> getArticlesByAuthorAndStatePaginated(User author, ArticleState state, Integer limit, Integer offset) {
//        return exerciseRepository.findArticlesByAuthorAndState(author, state, getDefaultPageable(limit, offset));
//    }


    public PageResponseDTO<ExerciseDTO> getPageWithExercisesByState(ExerciseState state, Integer limit, Integer offset) {
        Page<Exercise> exercisePage;

        if (state.equals(ExerciseState.ALL)) {
            exercisePage = getAllExercisesPaginated(limit, offset);
        } else {
            exercisePage = getExercisesByStatePaginated(state, limit, offset);
        }

        // return response after filtering
        return new PageResponseDTO<>(
                offset > 0 && exercisePage.getTotalElements() > 0,
                (offset + limit) < exercisePage.getTotalElements(),
                exercisePage.getContent().stream().map(ExerciseDTO::new).toList(),
                exercisePage.getTotalElements());
    }

    private Page<Exercise> getExercisesByStatePaginated(ExerciseState state, Integer limit, Integer offset) {
        return exerciseRepository.findExercisesByState(state, getDefaultPageable(limit, offset));
    }

    private Page<Exercise> getAllExercisesPaginated(Integer limit, Integer offset) {
        return exerciseRepository.findAll(getDefaultPageable(limit, offset));
    }


//    public boolean canUserEditArticle(User user, Article article) {
//        if (user == null) {
//            return false;
//        }
//        return user.is(RoleEnum.ADMIN) || article.getAuthor().equals(user);
//    }


    private Pageable getDefaultPageable(Integer limit, Integer offset) {
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate"));
    }
}
