package com.rustyard.ddnetstatustool;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerInfoViewModel extends ViewModel {
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_NO_PLAYER = 0;
    public static final int STATUS_TIMEOUT = -1;
    public static final int STATUS_NO_RESULT = -2;
    public static final int STATUS_REGEX_ERROR_t = -3;
    public static final int STATUS_REGEX_ERROR_m = -4;
    public static final int STATUS_REGEX_ERROR_w = -5;
    public static final int STATUS_REGEX_ERROR_n = -6;
    public static final int STATUS_REGEX_ERROR_mo = -7;
    public static final int STATUS_REGEX_ERROR_br = -8;
    public static final int STATUS_REGEX_ERROR_in = -9;
    public static final int STATUS_REGEX_ERROR_dm = -10;
    public static final int STATUS_REGEX_ERROR_dx = -11;
    public static final int STATUS_REGEX_ERROR_ol = -12;
    public static final int STATUS_REGEX_ERROR_so = -13;
    public static final int STATUS_REGEX_ERROR_ra = -14;

    public PlayerInfo player1Info;
    public PlayerInfo player2Info;

    public MutableLiveData<List<String>> p1InfoList;
    public MutableLiveData<List<String>> compareInfoList;

    // pattern used for "<rank>. with <points> points"
    private final String rpPatternString = "(\\d+)\\D+(\\d+)\\D+";
    private final Pattern rpPattern = Pattern.compile(rpPatternString);

    public PlayerInfoViewModel() {
        player1Info = new PlayerInfo();
        player2Info = new PlayerInfo();
        p1InfoList = new MutableLiveData<>();
        compareInfoList = new MutableLiveData<>();
    }

    /**
     * Crawl specified player data from ddnet.tw, store data into PlayerInfo class
     * @param playerID the player name(ID)
     * @param info the player info class, use player1Info or player2Info inside this class
     * @return crawl status
     */
    public int crawl(String playerID, PlayerInfo info) {
        Document ddnetPage = null;

        info.setPlayerID(UnicodeID.parse(playerID));

        try {
            ddnetPage = Jsoup.connect("https://ddnet.tw/players/" + playerID + "/").get();
        }
        catch (HttpStatusException httpStatusException) {
            return STATUS_NO_PLAYER;
        } catch (SocketTimeoutException timeoutException) {
            return STATUS_TIMEOUT;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ddnetPage != null) {
            return selectInfoFromDocument(ddnetPage, info);
        }
        else {
            return STATUS_NO_RESULT;
        }
    }

    private int selectInfoFromDocument(@NonNull Document ddnetPage, PlayerInfo info) {
        Matcher matcher;
        Elements element;

        // get ranks and points
        element = ddnetPage.select(".ladder:nth-child(1) .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setTotalPointsRank(matcher.group(1));
            info.setTotalPoints(matcher.group(2));
        } else return STATUS_REGEX_ERROR_t;


        // get the first finish record
        element = ddnetPage.select(".personal-result");
        info.setFirstFinish(element.text());

        // get the newest finish record
        element = ddnetPage.select(".block7+ .ladder tr:nth-child(1) td");
        info.setLastFinish(element.text());

        // get the monthly points ranking
        element = ddnetPage.select("#global br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setMonthlyRank(matcher.group(1));
            info.setMonthlyPoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setMonthlyRank("Unranked");
            info.setMonthlyPoints("0");
        }
        else return STATUS_REGEX_ERROR_m;

        // get the weekly points ranking
        element = ddnetPage.select("#global .ladder:nth-child(6) .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setWeeklyRank(matcher.group(1));
            info.setWeeklyPoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setWeeklyRank("Unranked");
            info.setWeeklyPoints("0");
        }
        else return STATUS_REGEX_ERROR_w;

        // TODO: add a online status (get information from status.tw)
        // NOTE: status.tw is slow to connect from China so this feature may be gave up.

        // TODO: add a map finish record searcher

        // TODO: add a country display
        // TODO: show the country in picture


        // Novice maps "rank and points"
        element = ddnetPage.select("#Novice br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setNoviceRank(matcher.group(1));
            info.setNovicePoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setNoviceRank("Unranked");
            info.setNovicePoints("0");
        }
        else return STATUS_REGEX_ERROR_n;

        // Moderate maps "rank and points"
        element = ddnetPage.select("#Moderate br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setModerateRank(matcher.group(1));
            info.setModeratePoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setModerateRank("Unranked");
            info.setModeratePoints("0");
        }
        else return STATUS_REGEX_ERROR_mo;

        // Brutal maps "rank and points"
        element = ddnetPage.select("#Brutal br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setBrutalRank(matcher.group(1));
            info.setBrutalPoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setBrutalRank("Unranked");
            info.setBrutalPoints("0");
        }
        else return STATUS_REGEX_ERROR_br;

        // Insane maps "rank and points"
        element = ddnetPage.select("#Insane br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setInsaneRank(matcher.group(1));
            info.setInsanePoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setInsaneRank("Unranked");
            info.setInsanePoints("0");
        }
        else return STATUS_REGEX_ERROR_in;

        // Dummy maps "rank and points"
        element = ddnetPage.select("#Dummy br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setDummyRank(matcher.group(1));
            info.setDummyPoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setDummyRank("Unranked");
            info.setDummyPoints("0");
        }
        else return STATUS_REGEX_ERROR_dm;

        // DDmaX maps "rank and points"
        element = ddnetPage.select("#DDmaX br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setDdmaxRank(matcher.group(1));
            info.setDdmaxPoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setDdmaxRank("Unranked");
            info.setDdmaxPoints("0");
        }
        else return STATUS_REGEX_ERROR_dx;

        // Oldschool maps "rank and points"
        element = ddnetPage.select("#Oldschool br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setOldschoolRank(matcher.group(1));
            info.setOldschoolPoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setOldschoolRank("Unranked");
            info.setOldschoolPoints("0");
        }
        else return STATUS_REGEX_ERROR_ol;

        // Solo maps "rank and points"
        element = ddnetPage.select("#Solo br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setSoloRank(matcher.group(1));
            info.setSoloPoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setSoloRank("Unranked");
            info.setSoloPoints("0");
        }else return STATUS_REGEX_ERROR_so;

        // Race maps "rank and points"
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            info.setRaceRank(matcher.group(1));
            info.setRacePoints(matcher.group(2));
        }
        else if (element.text().equals("Unranked")) {
            info.setRaceRank("Unranked");
            info.setRacePoints("0");
        }
        else return STATUS_REGEX_ERROR_ra;

        return STATUS_SUCCESS;
    }

    public void generateP1Info(Activity activity) {
        if (player1Info.getPlayerID().isEmpty()) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(activity.getString(R.string.textNoData));
            p1InfoList.setValue(temp);
            return;
        }

        ArrayList<String> infoList = new ArrayList<>();
        infoList.add(activity.getString(R.string.textPlayerID) + player1Info.getPlayerID());
        infoList.add(activity.getString(R.string.textPlayerTotalPoints) + player1Info.getTotalPoints());
        infoList.add(activity.getString(R.string.textPlayerTotalRank) + player1Info.getTotalPointsRank());
        infoList.add(activity.getString(R.string.textPlayerFirstFinish) + player1Info.getFirstFinish());
        infoList.add(activity.getString(R.string.textPlayerLastFinish) + player1Info.getLastFinish());
        infoList.add(activity.getString(R.string.textPlayerMonthlyPoints) + player1Info.getMonthlyPoints());
        infoList.add(activity.getString(R.string.textPlayerMonthlyRank) + player1Info.getMonthlyRank());
        infoList.add(activity.getString(R.string.textPlayerWeeklyPoints) + player1Info.getWeeklyPoints());
        infoList.add(activity.getString(R.string.textPlayerWeeklyRank) + player1Info.getWeeklyRank());
        infoList.add(activity.getString(R.string.textPlayerNovicePoints) + player1Info.getNovicePoints());
        infoList.add(activity.getString(R.string.textPlayerNoviceRank) + player1Info.getNoviceRank());
        infoList.add(activity.getString(R.string.textPlayerModeratePoints) + player1Info.getModeratePoints());
        infoList.add(activity.getString(R.string.textPlayerModerateRank) + player1Info.getModerateRank());
        infoList.add(activity.getString(R.string.textPlayerBrutalPoints) + player1Info.getBrutalPoints());
        infoList.add(activity.getString(R.string.textPlayerBrutalRank) + player1Info.getBrutalRank());
        infoList.add(activity.getString(R.string.textPlayerInsanePoints) + player1Info.getInsanePoints());
        infoList.add(activity.getString(R.string.textPlayerInsaneRank) + player1Info.getInsaneRank());
        infoList.add(activity.getString(R.string.textPlayerDummyPoints) + player1Info.getDummyPoints());
        infoList.add(activity.getString(R.string.textPlayerDummyRank) + player1Info.getDummyRank());
        infoList.add(activity.getString(R.string.textPlayerDDMaxPoints) + player1Info.getDdmaxPoints());
        infoList.add(activity.getString(R.string.textPlayerDDMaxRank) + player1Info.getDdmaxRank());
        infoList.add(activity.getString(R.string.textPlayerSoloPoints) + player1Info.getSoloPoints());
        infoList.add(activity.getString(R.string.textPlayerSoloRank) + player1Info.getSoloRank());
        infoList.add(activity.getString(R.string.textPlayerOldschoolPoints) + player1Info.getOldschoolPoints());
        infoList.add(activity.getString(R.string.textPlayerOldschoolRank) + player1Info.getOldschoolRank());
        infoList.add(activity.getString(R.string.textPlayerRacePoints) + player1Info.getRacePoints());
        infoList.add(activity.getString(R.string.textPlayerRaceRank) + player1Info.getRaceRank());
        // use postValue not setValue
        p1InfoList.postValue(infoList);
    }
}
