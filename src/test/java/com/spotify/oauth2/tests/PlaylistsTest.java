package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.utils.FakerUtils.generateName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Epic("Spotify Oauth2.0")
@Feature("Playlist API")
public class PlaylistsTest extends BaseTest {
    @Story("Create a playlist story")
    @Link("https://example.org")
    @Link(name = "allure", type = "mylink")
    @TmsLink("12345")
    @Issue("1234567")
    @Description("this is the description")
    @Test(description = "Should be able to create a playlist")
    public void shouldBeAbleToCreateAPlaylist(){
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201.getCode());

        Playlist responsePlaylist = response.as(Playlist.class);

        assertPlaylistEqual(requestPlaylist, responsePlaylist);
    }

    @Test(description = "Should be able to retrieve a playlist")
    public void shouldBeAbleToGetAPlaylist(){
        Playlist requestPlaylist = playlistBuilder("Updated", "This is updated", false);

        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistIdGet());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.getCode());

        Playlist responsePlaylist = response.as(Playlist.class);

        assertPlaylistEqual(requestPlaylist, responsePlaylist);
    }

    @Test(description = "Should be able to update a playlist")
    public void shouldBeAbleToUpdateAPlaylist(){
        Playlist requestPlaylist = playlistBuilder("Updated", "This is updated", false);

        Response response = PlaylistApi.update(DataLoader.getInstance().getPlaylistIdUpdate(), requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.getCode());

    }

    @Story("Create a playlist story")
    @Test(description = "Negative: Should not be able to create a playlist without name")
    public void shouldNotBeAbleToCreateAPlaylistWithoutName(){
        Playlist requestPlaylist = playlistBuilder("", generateDescription(), false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400.getCode());

        ErrorRoot errorResponse =  response.as(ErrorRoot.class);

        assertError(errorResponse, StatusCode.CODE_400.getCode(), StatusCode.CODE_400.getMsg());
    }

    @Story("Create a playlist story")
    @Test(description = "Negative: Should not be able to create a playlist with expired token")
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken(){
        String invalid_token = "12345";
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);

        Response response = PlaylistApi.post(requestPlaylist, invalid_token);
        assertStatusCode(response.statusCode(), StatusCode.CODE_401.getCode());

        ErrorRoot errorResponse = response.as(ErrorRoot.class);

        assertError(errorResponse, StatusCode.CODE_401.getCode(), StatusCode.CODE_401.getMsg());
    }

    @Step
    public Playlist playlistBuilder(String name, String description, Boolean _public){
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();
    }

    @Step
    public void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist){
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    @Step
    public void assertStatusCode(int responseStatusCode, int requestStatusCode){
        assertThat(responseStatusCode, equalTo(requestStatusCode));
    }

    @Step
    public void assertError(ErrorRoot responseErr, int expectedStatusCode, String expectedMsg){
        assertThat(responseErr.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(responseErr.getError().getMessage(), equalTo(expectedMsg));
    }
}
