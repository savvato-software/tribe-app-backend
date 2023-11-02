package com.savvato.tribeapp.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.savvato.tribeapp.entities.RejectedPhrase;
import com.savvato.tribeapp.entities.ReviewSubmittingUser;
import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.RejectedPhraseRepository;
import com.savvato.tribeapp.repositories.ReviewSubmittingUserRepository;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ToBeReviewedCheckerServiceImplTest extends AbstractServiceImplTest {
    @TestConfiguration
    static class ToBeReviewedCheckerServiceImplTestContextConfiguration {
        @Bean
        public ToBeReviewedCheckerService toBeReviewedCheckerService() {
            return new ToBeReviewedCheckerServiceImpl();
        }
    }

    @Autowired
    private ToBeReviewedCheckerService toBeReviewedCheckerService;

    @MockBean
    private PhraseServiceImpl phraseService;
    @MockBean
    private ToBeReviewedRepository toBeReviewedRepository;
    @MockBean
    private RejectedPhraseRepository rejectedPhraseRepository;

    @MockBean
    private ReviewSubmittingUserRepository reviewSubmittingUserRepository;

    @MockBean
    private RestTemplate restTemplate;


    @Test
    public void updateUngroomedPhrases() {
        ToBeReviewed tbr1 = new ToBeReviewed(1L, false, "competitively", "plays", "", "chess");
        ToBeReviewed tbr2 = new ToBeReviewed(2L, false, "enthusiastically", "volunteers", "for", "UNICEF");
        List<ToBeReviewed> ungroomedPhrases = List.of(tbr1, tbr2);
        ArgumentCaptor<ToBeReviewed> tbrArgumentCaptor = ArgumentCaptor.forClass(ToBeReviewed.class);
        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        when(toBeReviewedRepository.getAllUngroomed()).thenReturn(ungroomedPhrases);
        doNothing().when(toBeReviewedCheckerServiceSpy).validatePhrase(any(ToBeReviewed.class));
        toBeReviewedCheckerServiceSpy.updateUngroomedPhrases();
        verify(toBeReviewedCheckerServiceSpy, times(ungroomedPhrases.size())).validatePhrase(tbrArgumentCaptor.capture());
        assertThat(tbrArgumentCaptor.getAllValues()).usingRecursiveComparison().isEqualTo(ungroomedPhrases);
    }

    @Test
    public void getWordDetails() {
        String word = "competitively";
        String responseBody = "{\"word\":\"competitively\",\"results\":[{\"definition\":\"in competition\",\"partOfSpeech\":null,\"antonyms\":[\"noncompetitively\"],\"pertainsTo\":[\"competitive\"],\"examples\":[\"the companies ld bid competitively\"]}],\"syllables\":{\"count\":5,\"list\":[\"com\",\"pet\",\"i\",\"tive\",\"ly\"]},\"pronunciation\":{\"all\":\"kəm'pɛtɪtɪvli\"},\"frequency\":2.03}";
        ResponseEntity<String> response = new ResponseEntity<>(responseBody, HttpStatus.OK);
        JsonObject wordDetails =
                new JsonParser()
                        .parse(responseBody)
                        .getAsJsonObject();
        Optional<JsonObject> expected = Optional.of(wordDetails);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))).thenReturn(response);

        Optional<JsonObject> actual = toBeReviewedCheckerService.getWordDetails(word);

        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    public void getWordDetailsWhenErrorIsThrown() {
        String word = "competitively";
        String errorMessage = "Something went wrong.";
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))).thenThrow(new RestClientException(errorMessage));

        assertEquals(toBeReviewedCheckerService.getWordDetails(word), Optional.empty());
    }

    @Test
    public void checkPartOfSpeech() {
        String word = "walk";
        String expectedPartOfSpeech = "verb";
        JsonObject wordDetails =
                new JsonParser()
                        .parse(
                                "{\"word\":\"walk\",\"results\":[{\"definition\":\"the act of traveling by foot\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"walking\"],\"typeOf\":[\"locomotion\",\"travel\"],\"hasTypes\":[\"noctambulism\",\"march\",\"marching\",\"noctambulation\",\"plod\",\"plodding\",\"prowl\",\"shamble\",\"shambling\",\"shuffle\",\"shuffling\",\"sleepwalking\",\"somnambulation\",\"somnambulism\",\"wading\",\"ambulation\",\"gait\"],\"hasParts\":[\"pace\",\"tread\",\"stride\"],\"examples\":[\"walking is a healthy form of exercise\"]},{\"definition\":\"careers in general\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"walk of life\"],\"typeOf\":[\"calling\",\"career\",\"vocation\"],\"examples\":[\"it happens in all walks of life\"]},{\"definition\":\"take a walk; go for a walk; walk for pleasure\",\"partOfSpeech\":\"verb\",\"synonyms\":[\"take the air\"],\"typeOf\":[\"move\",\"locomote\",\"go\",\"travel\"],\"hasTypes\":[\"constitutionalize\"],\"examples\":[\"The lovers held hands while walking\",\"We like to walk every Sunday\"]},{\"definition\":\"manner of walking\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"manner of walking\"],\"typeOf\":[\"posture\",\"bearing\",\"carriage\"],\"examples\":[\"he had a funny walk\"]},{\"definition\":\"(baseball) an advance to first base by a batter who receives four balls\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"base on balls\",\"pass\"],\"inCategory\":[\"baseball\",\"baseball game\",\"ball\"],\"typeOf\":[\"achievement\",\"accomplishment\"]},{\"definition\":\"a path set aside for walking\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"paseo\",\"walkway\"],\"typeOf\":[\"path\"],\"hasTypes\":[\"sidewalk\",\"skywalk\",\"flagging\",\"ambulatory\",\"catwalk\",\"pavement\",\"promenade\",\"mall\",\"boardwalk\"],\"examples\":[\"after the blizzard he shoveled the front walk\"]},{\"definition\":\"accompany or escort\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"accompany\"],\"hasTypes\":[\"trot\",\"march\"],\"examples\":[\"I'll walk you to your car\"]},{\"definition\":\"a slow gait of a horse in which two feet are always on the ground\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"gait\"]},{\"definition\":\"be or act in association with\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"consociate\",\"associate\"],\"examples\":[\"We must walk with our dispossessed brothers and sisters\"]},{\"definition\":\"give a base on balls to\",\"partOfSpeech\":\"verb\",\"inCategory\":[\"ball\",\"baseball game\",\"baseball\"],\"typeOf\":[\"play\"]},{\"definition\":\"live or behave in a specified manner\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"comport\",\"behave\"],\"examples\":[\"walk in sadness\"]},{\"definition\":\"make walk\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"oblige\",\"obligate\",\"compel\"],\"hasTypes\":[\"march\",\"exhibit\",\"parade\"],\"examples\":[\"He walks the horse up the mountain\"]},{\"definition\":\"obtain a base on balls\",\"partOfSpeech\":\"verb\",\"inCategory\":[\"baseball game\",\"baseball\",\"ball\"],\"typeOf\":[\"tally\",\"score\",\"hit\",\"rack up\"]},{\"definition\":\"the act of walking somewhere\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"travelling\",\"traveling\",\"travel\"],\"hasTypes\":[\"foot\",\"walkabout\",\"walk-through\",\"turn\",\"moonwalk\",\"tramp\",\"stroll\",\"hiking\",\"amble\",\"saunter\",\"last mile\",\"promenade\",\"perambulation\",\"hike\",\"constitutional\"],\"examples\":[\"he took a walk after lunch\"]},{\"definition\":\"traverse or cover by walking\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"traverse\",\"cut across\",\"cut through\",\"cover\",\"track\",\"get across\",\"get over\",\"pass over\",\"cross\"],\"derivation\":[\"walker\"],\"examples\":[\"Paul walked the streets of Damascus\",\"She walks 3 miles every day\"]},{\"definition\":\"use one's feet to advance; advance by steps\",\"partOfSpeech\":\"verb\",\"entails\":[\"step\"],\"typeOf\":[\"go\",\"locomote\",\"travel\",\"move\"],\"hasTypes\":[\"walk around\",\"amble\",\"ambulate\",\"bumble\",\"careen\",\"clomp\",\"clump\",\"cock\",\"coggle\",\"creep\",\"dodder\",\"falter\",\"flounce\",\"flounder\",\"foot\",\"footslog\",\"gimp\",\"hike\",\"hitch\",\"hobble\",\"hoof\",\"hoof it\",\"keel\",\"leg it\",\"limp\",\"lollop\",\"lumber\",\"lurch\",\"march\",\"mince\",\"mosey\",\"mouse\",\"pace\",\"pad\",\"paddle\",\"perambulate\",\"plod\",\"pound\",\"prance\",\"process\",\"promenade\",\"prowl\",\"pussyfoot\",\"reel\",\"ruffle\",\"sashay\",\"saunter\",\"scuffle\",\"shamble\",\"shlep\",\"shuffle\",\"skulk\",\"sleepwalk\",\"slink\",\"slog\",\"slouch\",\"sneak\",\"somnambulate\",\"spacewalk\",\"stagger\",\"stalk\",\"stamp\",\"step\",\"stomp\",\"stride\",\"stroll\",\"strut\",\"stumble\",\"stump\",\"swag\",\"swagger\",\"tap\",\"tip\",\"tippytoe\",\"tiptoe\",\"tittup\",\"toddle\",\"toe\",\"totter\",\"traipse\",\"tramp\",\"tramp down\",\"trample\",\"tread\",\"tread down\",\"trudge\",\"waddle\",\"wade\",\"walk about\"],\"verbGroup\":[\"take the air\"],\"antonyms\":[\"ride\"],\"also\":[\"walk about\",\"walk around\"],\"derivation\":[\"walking\",\"walker\"],\"examples\":[\"We walked instead of driving\",\"She walks with a slight limp\",\"The patient cannot walk yet\"]},{\"definition\":\"walk at a pace\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"pace\"],\"examples\":[\"The horses walked across the meadow\"]}],\"syllables\":{\"count\":1,\"list\":[\"walk\"]},\"pronunciation\":{\"all\":\"wɔk\"},\"frequency\":5.3}")
                        .getAsJsonObject();
        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(Optional.of(wordDetails)).when(toBeReviewedCheckerServiceSpy).getWordDetails(word);
        boolean rtn = toBeReviewedCheckerServiceSpy.checkPartOfSpeech(word, expectedPartOfSpeech);
        assertTrue(rtn);
    }

    @Test
    public void checkPartOfSpeechWhenNoDefinitionFound() {
        String word = "walk";
        String expectedPartOfSpeech = "verb";
        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(Optional.empty()).when(toBeReviewedCheckerServiceSpy).getWordDetails(word);
        boolean rtn = toBeReviewedCheckerServiceSpy.checkPartOfSpeech(word, expectedPartOfSpeech);
        assertFalse(rtn);
    }

    @Test
    public void checkPartOfSpeechWhenExpectedPartOfSpeechDoesntMatchWord() {
        String word = "walk";
        String expectedPartOfSpeech = "a part of speech that doesn't match the word details";
        JsonObject wordDetails =
                new JsonParser()
                        .parse(
                                "{\"word\":\"walk\",\"results\":[{\"definition\":\"the act of traveling by foot\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"walking\"],\"typeOf\":[\"locomotion\",\"travel\"],\"hasTypes\":[\"noctambulism\",\"march\",\"marching\",\"noctambulation\",\"plod\",\"plodding\",\"prowl\",\"shamble\",\"shambling\",\"shuffle\",\"shuffling\",\"sleepwalking\",\"somnambulation\",\"somnambulism\",\"wading\",\"ambulation\",\"gait\"],\"hasParts\":[\"pace\",\"tread\",\"stride\"],\"examples\":[\"walking is a healthy form of exercise\"]},{\"definition\":\"careers in general\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"walk of life\"],\"typeOf\":[\"calling\",\"career\",\"vocation\"],\"examples\":[\"it happens in all walks of life\"]},{\"definition\":\"take a walk; go for a walk; walk for pleasure\",\"partOfSpeech\":\"verb\",\"synonyms\":[\"take the air\"],\"typeOf\":[\"move\",\"locomote\",\"go\",\"travel\"],\"hasTypes\":[\"constitutionalize\"],\"examples\":[\"The lovers held hands while walking\",\"We like to walk every Sunday\"]},{\"definition\":\"manner of walking\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"manner of walking\"],\"typeOf\":[\"posture\",\"bearing\",\"carriage\"],\"examples\":[\"he had a funny walk\"]},{\"definition\":\"(baseball) an advance to first base by a batter who receives four balls\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"base on balls\",\"pass\"],\"inCategory\":[\"baseball\",\"baseball game\",\"ball\"],\"typeOf\":[\"achievement\",\"accomplishment\"]},{\"definition\":\"a path set aside for walking\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"paseo\",\"walkway\"],\"typeOf\":[\"path\"],\"hasTypes\":[\"sidewalk\",\"skywalk\",\"flagging\",\"ambulatory\",\"catwalk\",\"pavement\",\"promenade\",\"mall\",\"boardwalk\"],\"examples\":[\"after the blizzard he shoveled the front walk\"]},{\"definition\":\"accompany or escort\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"accompany\"],\"hasTypes\":[\"trot\",\"march\"],\"examples\":[\"I'll walk you to your car\"]},{\"definition\":\"a slow gait of a horse in which two feet are always on the ground\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"gait\"]},{\"definition\":\"be or act in association with\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"consociate\",\"associate\"],\"examples\":[\"We must walk with our dispossessed brothers and sisters\"]},{\"definition\":\"give a base on balls to\",\"partOfSpeech\":\"verb\",\"inCategory\":[\"ball\",\"baseball game\",\"baseball\"],\"typeOf\":[\"play\"]},{\"definition\":\"live or behave in a specified manner\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"comport\",\"behave\"],\"examples\":[\"walk in sadness\"]},{\"definition\":\"make walk\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"oblige\",\"obligate\",\"compel\"],\"hasTypes\":[\"march\",\"exhibit\",\"parade\"],\"examples\":[\"He walks the horse up the mountain\"]},{\"definition\":\"obtain a base on balls\",\"partOfSpeech\":\"verb\",\"inCategory\":[\"baseball game\",\"baseball\",\"ball\"],\"typeOf\":[\"tally\",\"score\",\"hit\",\"rack up\"]},{\"definition\":\"the act of walking somewhere\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"travelling\",\"traveling\",\"travel\"],\"hasTypes\":[\"foot\",\"walkabout\",\"walk-through\",\"turn\",\"moonwalk\",\"tramp\",\"stroll\",\"hiking\",\"amble\",\"saunter\",\"last mile\",\"promenade\",\"perambulation\",\"hike\",\"constitutional\"],\"examples\":[\"he took a walk after lunch\"]},{\"definition\":\"traverse or cover by walking\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"traverse\",\"cut across\",\"cut through\",\"cover\",\"track\",\"get across\",\"get over\",\"pass over\",\"cross\"],\"derivation\":[\"walker\"],\"examples\":[\"Paul walked the streets of Damascus\",\"She walks 3 miles every day\"]},{\"definition\":\"use one's feet to advance; advance by steps\",\"partOfSpeech\":\"verb\",\"entails\":[\"step\"],\"typeOf\":[\"go\",\"locomote\",\"travel\",\"move\"],\"hasTypes\":[\"walk around\",\"amble\",\"ambulate\",\"bumble\",\"careen\",\"clomp\",\"clump\",\"cock\",\"coggle\",\"creep\",\"dodder\",\"falter\",\"flounce\",\"flounder\",\"foot\",\"footslog\",\"gimp\",\"hike\",\"hitch\",\"hobble\",\"hoof\",\"hoof it\",\"keel\",\"leg it\",\"limp\",\"lollop\",\"lumber\",\"lurch\",\"march\",\"mince\",\"mosey\",\"mouse\",\"pace\",\"pad\",\"paddle\",\"perambulate\",\"plod\",\"pound\",\"prance\",\"process\",\"promenade\",\"prowl\",\"pussyfoot\",\"reel\",\"ruffle\",\"sashay\",\"saunter\",\"scuffle\",\"shamble\",\"shlep\",\"shuffle\",\"skulk\",\"sleepwalk\",\"slink\",\"slog\",\"slouch\",\"sneak\",\"somnambulate\",\"spacewalk\",\"stagger\",\"stalk\",\"stamp\",\"step\",\"stomp\",\"stride\",\"stroll\",\"strut\",\"stumble\",\"stump\",\"swag\",\"swagger\",\"tap\",\"tip\",\"tippytoe\",\"tiptoe\",\"tittup\",\"toddle\",\"toe\",\"totter\",\"traipse\",\"tramp\",\"tramp down\",\"trample\",\"tread\",\"tread down\",\"trudge\",\"waddle\",\"wade\",\"walk about\"],\"verbGroup\":[\"take the air\"],\"antonyms\":[\"ride\"],\"also\":[\"walk about\",\"walk around\"],\"derivation\":[\"walking\",\"walker\"],\"examples\":[\"We walked instead of driving\",\"She walks with a slight limp\",\"The patient cannot walk yet\"]},{\"definition\":\"walk at a pace\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"pace\"],\"examples\":[\"The horses walked across the meadow\"]}],\"syllables\":{\"count\":1,\"list\":[\"walk\"]},\"pronunciation\":{\"all\":\"wɔk\"},\"frequency\":5.3}")
                        .getAsJsonObject();
        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(Optional.of(wordDetails)).when(toBeReviewedCheckerServiceSpy).getWordDetails(word);
        boolean rtn = toBeReviewedCheckerServiceSpy.checkPartOfSpeech(word, expectedPartOfSpeech);
        assertFalse(rtn);
    }


    @Test
    public void validatePhraseComponentWhenComponentIsValidNoun() {
        String word = "walk";
        String expectedPartOfSpeech = "noun";
        JsonObject wordDetails =
                new JsonParser()
                        .parse(
                                "{\"word\":\"walk\",\"results\":[{\"definition\":\"the act of traveling by foot\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"walking\"],\"typeOf\":[\"locomotion\",\"travel\"],\"hasTypes\":[\"noctambulism\",\"march\",\"marching\",\"noctambulation\",\"plod\",\"plodding\",\"prowl\",\"shamble\",\"shambling\",\"shuffle\",\"shuffling\",\"sleepwalking\",\"somnambulation\",\"somnambulism\",\"wading\",\"ambulation\",\"gait\"],\"hasParts\":[\"pace\",\"tread\",\"stride\"],\"examples\":[\"walking is a healthy form of exercise\"]},{\"definition\":\"careers in general\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"walk of life\"],\"typeOf\":[\"calling\",\"career\",\"vocation\"],\"examples\":[\"it happens in all walks of life\"]},{\"definition\":\"take a walk; go for a walk; walk for pleasure\",\"partOfSpeech\":\"verb\",\"synonyms\":[\"take the air\"],\"typeOf\":[\"move\",\"locomote\",\"go\",\"travel\"],\"hasTypes\":[\"constitutionalize\"],\"examples\":[\"The lovers held hands while walking\",\"We like to walk every Sunday\"]},{\"definition\":\"manner of walking\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"manner of walking\"],\"typeOf\":[\"posture\",\"bearing\",\"carriage\"],\"examples\":[\"he had a funny walk\"]},{\"definition\":\"(baseball) an advance to first base by a batter who receives four balls\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"base on balls\",\"pass\"],\"inCategory\":[\"baseball\",\"baseball game\",\"ball\"],\"typeOf\":[\"achievement\",\"accomplishment\"]},{\"definition\":\"a path set aside for walking\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"paseo\",\"walkway\"],\"typeOf\":[\"path\"],\"hasTypes\":[\"sidewalk\",\"skywalk\",\"flagging\",\"ambulatory\",\"catwalk\",\"pavement\",\"promenade\",\"mall\",\"boardwalk\"],\"examples\":[\"after the blizzard he shoveled the front walk\"]},{\"definition\":\"accompany or escort\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"accompany\"],\"hasTypes\":[\"trot\",\"march\"],\"examples\":[\"I'll walk you to your car\"]},{\"definition\":\"a slow gait of a horse in which two feet are always on the ground\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"gait\"]},{\"definition\":\"be or act in association with\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"consociate\",\"associate\"],\"examples\":[\"We must walk with our dispossessed brothers and sisters\"]},{\"definition\":\"give a base on balls to\",\"partOfSpeech\":\"verb\",\"inCategory\":[\"ball\",\"baseball game\",\"baseball\"],\"typeOf\":[\"play\"]},{\"definition\":\"live or behave in a specified manner\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"comport\",\"behave\"],\"examples\":[\"walk in sadness\"]},{\"definition\":\"make walk\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"oblige\",\"obligate\",\"compel\"],\"hasTypes\":[\"march\",\"exhibit\",\"parade\"],\"examples\":[\"He walks the horse up the mountain\"]},{\"definition\":\"obtain a base on balls\",\"partOfSpeech\":\"verb\",\"inCategory\":[\"baseball game\",\"baseball\",\"ball\"],\"typeOf\":[\"tally\",\"score\",\"hit\",\"rack up\"]},{\"definition\":\"the act of walking somewhere\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"travelling\",\"traveling\",\"travel\"],\"hasTypes\":[\"foot\",\"walkabout\",\"walk-through\",\"turn\",\"moonwalk\",\"tramp\",\"stroll\",\"hiking\",\"amble\",\"saunter\",\"last mile\",\"promenade\",\"perambulation\",\"hike\",\"constitutional\"],\"examples\":[\"he took a walk after lunch\"]},{\"definition\":\"traverse or cover by walking\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"traverse\",\"cut across\",\"cut through\",\"cover\",\"track\",\"get across\",\"get over\",\"pass over\",\"cross\"],\"derivation\":[\"walker\"],\"examples\":[\"Paul walked the streets of Damascus\",\"She walks 3 miles every day\"]},{\"definition\":\"use one's feet to advance; advance by steps\",\"partOfSpeech\":\"verb\",\"entails\":[\"step\"],\"typeOf\":[\"go\",\"locomote\",\"travel\",\"move\"],\"hasTypes\":[\"walk around\",\"amble\",\"ambulate\",\"bumble\",\"careen\",\"clomp\",\"clump\",\"cock\",\"coggle\",\"creep\",\"dodder\",\"falter\",\"flounce\",\"flounder\",\"foot\",\"footslog\",\"gimp\",\"hike\",\"hitch\",\"hobble\",\"hoof\",\"hoof it\",\"keel\",\"leg it\",\"limp\",\"lollop\",\"lumber\",\"lurch\",\"march\",\"mince\",\"mosey\",\"mouse\",\"pace\",\"pad\",\"paddle\",\"perambulate\",\"plod\",\"pound\",\"prance\",\"process\",\"promenade\",\"prowl\",\"pussyfoot\",\"reel\",\"ruffle\",\"sashay\",\"saunter\",\"scuffle\",\"shamble\",\"shlep\",\"shuffle\",\"skulk\",\"sleepwalk\",\"slink\",\"slog\",\"slouch\",\"sneak\",\"somnambulate\",\"spacewalk\",\"stagger\",\"stalk\",\"stamp\",\"step\",\"stomp\",\"stride\",\"stroll\",\"strut\",\"stumble\",\"stump\",\"swag\",\"swagger\",\"tap\",\"tip\",\"tippytoe\",\"tiptoe\",\"tittup\",\"toddle\",\"toe\",\"totter\",\"traipse\",\"tramp\",\"tramp down\",\"trample\",\"tread\",\"tread down\",\"trudge\",\"waddle\",\"wade\",\"walk about\"],\"verbGroup\":[\"take the air\"],\"antonyms\":[\"ride\"],\"also\":[\"walk about\",\"walk around\"],\"derivation\":[\"walking\",\"walker\"],\"examples\":[\"We walked instead of driving\",\"She walks with a slight limp\",\"The patient cannot walk yet\"]},{\"definition\":\"walk at a pace\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"pace\"],\"examples\":[\"The horses walked across the meadow\"]}],\"syllables\":{\"count\":1,\"list\":[\"walk\"]},\"pronunciation\":{\"all\":\"wɔk\"},\"frequency\":5.3}")
                        .getAsJsonObject();

        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(Optional.of(wordDetails)).when(toBeReviewedCheckerServiceSpy).getWordDetails(word);
        doReturn(true)
                .when(toBeReviewedCheckerServiceSpy)
                .checkPartOfSpeech(anyString(), anyString());

        boolean rtn = toBeReviewedCheckerServiceSpy.checkPartOfSpeech(word, expectedPartOfSpeech);
        assertTrue(rtn);
    }

    @Test
    public void validatePhraseComponentWhenComponentIsIncorrectPartOfSpeech() {
        String word = "is";
        String expectedPartOfSpeech = "noun";
        JsonObject wordDetails =
                new JsonParser()
                        .parse(
                                "{\"word\":\"is\",\"results\":[{\"definition\":\"the act of traveling by foot\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"walking\"],\"typeOf\":[\"locomotion\",\"travel\"],\"hasTypes\":[\"noctambulism\",\"march\",\"marching\",\"noctambulation\",\"plod\",\"plodding\",\"prowl\",\"shamble\",\"shambling\",\"shuffle\",\"shuffling\",\"sleepwalking\",\"somnambulation\",\"somnambulism\",\"wading\",\"ambulation\",\"gait\"],\"hasParts\":[\"pace\",\"tread\",\"stride\"],\"examples\":[\"walking is a healthy form of exercise\"]},{\"definition\":\"careers in general\",\"partOfSpeech\":\"verb\",\"synonyms\":[\"walk of life\"],\"typeOf\":[\"calling\",\"career\",\"vocation\"],\"examples\":[\"it happens in all walks of life\"]},{\"definition\":\"take a walk; go for a walk; walk for pleasure\",\"partOfSpeech\":\"verb\",\"synonyms\":[\"take the air\"],\"typeOf\":[\"move\",\"locomote\",\"go\",\"travel\"],\"hasTypes\":[\"constitutionalize\"],\"examples\":[\"The lovers held hands while walking\",\"We like to walk every Sunday\"]},{\"definition\":\"manner of walking\",\"partOfSpeech\":\"verb\",\"synonyms\":[\"manner of walking\"],\"typeOf\":[\"posture\",\"bearing\",\"carriage\"],\"examples\":[\"he had a funny walk\"]},{\"definition\":\"(baseball) an advance to first base by a batter who receives four balls\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"base on balls\",\"pass\"],\"inCategory\":[\"baseball\",\"baseball game\",\"ball\"],\"typeOf\":[\"achievement\",\"accomplishment\"]},{\"definition\":\"a path set aside for walking\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"paseo\",\"walkway\"],\"typeOf\":[\"path\"],\"hasTypes\":[\"sidewalk\",\"skywalk\",\"flagging\",\"ambulatory\",\"catwalk\",\"pavement\",\"promenade\",\"mall\",\"boardwalk\"],\"examples\":[\"after the blizzard he shoveled the front walk\"]},{\"definition\":\"accompany or escort\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"accompany\"],\"hasTypes\":[\"trot\",\"march\"],\"examples\":[\"I'll walk you to your car\"]},{\"definition\":\"a slow gait of a horse in which two feet are always on the ground\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"gait\"]},{\"definition\":\"be or act in association with\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"consociate\",\"associate\"],\"examples\":[\"We must walk with our dispossessed brothers and sisters\"]},{\"definition\":\"give a base on balls to\",\"partOfSpeech\":\"verb\",\"inCategory\":[\"ball\",\"baseball game\",\"baseball\"],\"typeOf\":[\"play\"]},{\"definition\":\"live or behave in a specified manner\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"comport\",\"behave\"],\"examples\":[\"walk in sadness\"]},{\"definition\":\"make walk\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"oblige\",\"obligate\",\"compel\"],\"hasTypes\":[\"march\",\"exhibit\",\"parade\"],\"examples\":[\"He walks the horse up the mountain\"]},{\"definition\":\"obtain a base on balls\",\"partOfSpeech\":\"verb\",\"inCategory\":[\"baseball game\",\"baseball\",\"ball\"],\"typeOf\":[\"tally\",\"score\",\"hit\",\"rack up\"]},{\"definition\":\"the act of walking somewhere\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"travelling\",\"traveling\",\"travel\"],\"hasTypes\":[\"foot\",\"walkabout\",\"walk-through\",\"turn\",\"moonwalk\",\"tramp\",\"stroll\",\"hiking\",\"amble\",\"saunter\",\"last mile\",\"promenade\",\"perambulation\",\"hike\",\"constitutional\"],\"examples\":[\"he took a walk after lunch\"]},{\"definition\":\"traverse or cover by walking\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"traverse\",\"cut across\",\"cut through\",\"cover\",\"track\",\"get across\",\"get over\",\"pass over\",\"cross\"],\"derivation\":[\"walker\"],\"examples\":[\"Paul walked the streets of Damascus\",\"She walks 3 miles every day\"]},{\"definition\":\"use one's feet to advance; advance by steps\",\"partOfSpeech\":\"verb\",\"entails\":[\"step\"],\"typeOf\":[\"go\",\"locomote\",\"travel\",\"move\"],\"hasTypes\":[\"walk around\",\"amble\",\"ambulate\",\"bumble\",\"careen\",\"clomp\",\"clump\",\"cock\",\"coggle\",\"creep\",\"dodder\",\"falter\",\"flounce\",\"flounder\",\"foot\",\"footslog\",\"gimp\",\"hike\",\"hitch\",\"hobble\",\"hoof\",\"hoof it\",\"keel\",\"leg it\",\"limp\",\"lollop\",\"lumber\",\"lurch\",\"march\",\"mince\",\"mosey\",\"mouse\",\"pace\",\"pad\",\"paddle\",\"perambulate\",\"plod\",\"pound\",\"prance\",\"process\",\"promenade\",\"prowl\",\"pussyfoot\",\"reel\",\"ruffle\",\"sashay\",\"saunter\",\"scuffle\",\"shamble\",\"shlep\",\"shuffle\",\"skulk\",\"sleepwalk\",\"slink\",\"slog\",\"slouch\",\"sneak\",\"somnambulate\",\"spacewalk\",\"stagger\",\"stalk\",\"stamp\",\"step\",\"stomp\",\"stride\",\"stroll\",\"strut\",\"stumble\",\"stump\",\"swag\",\"swagger\",\"tap\",\"tip\",\"tippytoe\",\"tiptoe\",\"tittup\",\"toddle\",\"toe\",\"totter\",\"traipse\",\"tramp\",\"tramp down\",\"trample\",\"tread\",\"tread down\",\"trudge\",\"waddle\",\"wade\",\"walk about\"],\"verbGroup\":[\"take the air\"],\"antonyms\":[\"ride\"],\"also\":[\"walk about\",\"walk around\"],\"derivation\":[\"walking\",\"walker\"],\"examples\":[\"We walked instead of driving\",\"She walks with a slight limp\",\"The patient cannot walk yet\"]},{\"definition\":\"walk at a pace\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"pace\"],\"examples\":[\"The horses walked across the meadow\"]}],\"syllables\":{\"count\":1,\"list\":[\"walk\"]},\"pronunciation\":{\"all\":\"wɔk\"},\"frequency\":5.3}")
                        .getAsJsonObject();

        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(Optional.of(wordDetails)).when(toBeReviewedCheckerServiceSpy).getWordDetails(word);
        doReturn(false)
                .when(toBeReviewedCheckerServiceSpy)
                .checkPartOfSpeech(anyString(), anyString());

        boolean rtn = toBeReviewedCheckerServiceSpy.checkPartOfSpeech(word, expectedPartOfSpeech);
        assertFalse(rtn);
    }

    @Test
    public void validatePhraseWhenPhraseValid() {
        ToBeReviewed tbr = new ToBeReviewed(1L, false, "competitively", "plays", "", "chess");
        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(true)
                .when(toBeReviewedCheckerServiceSpy)
                .checkPartOfSpeech(Mockito.any(), Mockito.any());
        Mockito.when(rejectedPhraseRepository.findByRejectedPhrase(Mockito.any()))
                .thenReturn(Optional.empty());
        toBeReviewedCheckerServiceSpy.validatePhrase(tbr);
        ArgumentCaptor<ToBeReviewed> tbrArgumentCaptor = ArgumentCaptor.forClass(ToBeReviewed.class);
        // verify tbr was groomed
        assertEquals(true, tbr.isHasBeenGroomed());

        verify(rejectedPhraseRepository, times(0)).save(Mockito.any());
        verify(reviewSubmittingUserRepository, times(0)).delete(Mockito.any());
        verify(toBeReviewedRepository, times(0)).deleteById(Mockito.any());
        verify(toBeReviewedRepository, times(1)).save(tbrArgumentCaptor.capture());
        assertThat(tbrArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(tbr);
    }

    @Test
    public void updateTablesWhenPhraseInvalidAndNoMatchingRejectedPhrase() {
        ToBeReviewed tbr = new ToBeReviewed(1L, false, "nonsense", "nonsense", "nonsense", "nonsense");
        Mockito.when(reviewSubmittingUserRepository.findUserIdByToBeReviewedId(Mockito.any()))
                .thenReturn(USER1_ID);

        toBeReviewedCheckerService.updateTables(tbr);

        ArgumentCaptor<RejectedPhrase> arg1 = ArgumentCaptor.forClass(RejectedPhrase.class);
        verify(rejectedPhraseRepository, times(1)).save(arg1.capture());
        assertEquals(arg1.getValue().getRejectedPhrase(), tbr.toString());

        ArgumentCaptor<ReviewSubmittingUser> arg2 = ArgumentCaptor.forClass(ReviewSubmittingUser.class);
        verify(reviewSubmittingUserRepository, times(1)).delete(arg2.capture());
        assertEquals(arg2.getValue().getUserId(), USER1_ID);

        ArgumentCaptor<Long> arg3 = ArgumentCaptor.forClass(Long.class);
        verify(toBeReviewedRepository, times(1)).deleteById(arg3.capture());
        assertEquals(arg3.getValue(), tbr.getId());

        // verify tbr was not groomed
        assertEquals(false, tbr.isHasBeenGroomed());
    }

    @Test
    public void validatePhraseWhenNoMatchingRejectedPhrase() {
        ToBeReviewed tbr = new ToBeReviewed(1L, false, "competitively", "plays", "", "chess");

        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        ArgumentCaptor<ToBeReviewed> tbrArgumentCaptor = ArgumentCaptor.forClass(ToBeReviewed.class);
        Mockito.when(rejectedPhraseRepository.findByRejectedPhrase(Mockito.any()))
                .thenReturn(Optional.empty());
        doReturn(true)
                .when(toBeReviewedCheckerServiceSpy)
                .checkPartOfSpeech(Mockito.any(), Mockito.any());

        toBeReviewedCheckerServiceSpy.validatePhrase(tbr);
        ArgumentCaptor<Long> arg1 = ArgumentCaptor.forClass(Long.class);

        // verify tbr was groomed
        assertEquals(true, tbr.isHasBeenGroomed());
        verify(toBeReviewedRepository, times(1)).save(tbrArgumentCaptor.capture());
        assertThat(tbrArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(tbr);
    }

    @Test
    public void verifyWordWithKnownMissingResultsSetIsSetForManualReview() {
        String word = "bakes";
        String expectedPartOfSpeech = "verb";

        // Known example of error from Words API as of 08/22/23
        JsonObject wordDetails =
                new JsonParser()
                        .parse(
                                "{\"rhymes\":{\"all\": \"-eɪkeɪʃ\"},\"word\":\"bakes\",\"pronunciation\":\"beɪks\",\"frequency\":2.78}")
                        .getAsJsonObject();

        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(Optional.of(wordDetails)).when(toBeReviewedCheckerServiceSpy).getWordDetails(word);

        boolean rtn = toBeReviewedCheckerServiceSpy.checkPartOfSpeech(word, expectedPartOfSpeech);
        assertTrue(rtn);
    }

    @Test
    public void verifyWordWithKnownMissingPartOfSpeechIsSetForManualReview() {
        String word = "competitively";
        String expectedPartOfSpeech = "adverb";

        // Known example of error from Words API as of 08/22/23
        JsonObject wordDetails =
                new JsonParser()
                        .parse(
                                "{\"word\":\"competitively\",\"results\":[{\"definition\":\"in competition\",\"partOfSpeech\":null,\"antonyms\":[\"noncompetitively\"],\"pertainsTo\":[\"competitive\"],\"examples\":[\"the companies ld bid competitively\"]}],\"syllables\":{\"count\":5,\"list\":[\"com\",\"pet\",\"i\",\"tive\",\"ly\"]},\"pronunciation\":{\"all\":\"kəm'pɛtɪtɪvli\"},\"frequency\":2.03}")
                        .getAsJsonObject();

        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(Optional.of(wordDetails)).when(toBeReviewedCheckerServiceSpy).getWordDetails(word);

        boolean rtn = toBeReviewedCheckerServiceSpy.checkPartOfSpeech(word, expectedPartOfSpeech);
        assertTrue(rtn);
    }

    @Test
    public void updatesTablesIfPhraseInvalid() {
        ToBeReviewed tbr = new ToBeReviewed(1L, false, "competitively", "plays", "", "chess");
        ToBeReviewedCheckerService toBeReviewedCheckerServiceSpy = spy(toBeReviewedCheckerService);
        doReturn(false)
                .when(toBeReviewedCheckerServiceSpy)
                .checkPartOfSpeech(Mockito.any(), Mockito.any());
        Mockito.when(rejectedPhraseRepository.findByRejectedPhrase(Mockito.any()))
                .thenReturn(Optional.empty());
        toBeReviewedCheckerServiceSpy.validatePhrase(tbr);
        ArgumentCaptor<ToBeReviewed> toBeReviewedArgumentCaptor =
                ArgumentCaptor.forClass(ToBeReviewed.class);

        verify(toBeReviewedRepository, times(0)).save(Mockito.any());
        verify(toBeReviewedCheckerServiceSpy, times(1))
                .updateTables(toBeReviewedArgumentCaptor.capture());
        assertEquals(toBeReviewedArgumentCaptor.getValue(), tbr);
    }
}
