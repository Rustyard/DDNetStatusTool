package com.rustyard.ddnetstatustool;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
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


    private String playerID         = "";

    private String totalPointsRank  = "";
    private String totalPoints      = "";
    private String lastFinish       = "";
    private String firstFinish      = "";
    private String monthlyPoints    = "";
    private String monthlyRank      = "";
    private String weeklyPoints     = "";
    private String weeklyRank       = "";
    private String novicePoints     = "";
    private String noviceRank       = "";
    private String moderatePoints   = "";
    private String moderateRank     = "";
    private String brutalPoints     = "";
    private String brutalRank       = "";
    private String insanePoints     = "";
    private String insaneRank       = "";
    private String dummyPoints      = "";
    private String dummyRank        = "";
    private String ddmaxPoints      = "";
    private String ddmaxRank        = "";
    private String soloPoints       = "";
    private String soloRank         = "";
    private String oldschoolPoints  = "";
    private String oldschoolRank    = "";
    private String racePoints       = "";
    private String raceRank         = "";

    // pattern used for "<rank>. with <points> points"
    private final String rpPatternString = "(\\d+)\\D+(\\d+)\\D+";
    private final Pattern rpPattern = Pattern.compile(rpPatternString);

    /**
     * Crawl specified player data from ddnet.tw
     * @param playerID the player name(ID)
     * @return crawl status
     */
    public int crawl(String playerID) {
        Document ddnetPage = null;

        this.playerID = playerID;

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
            return selectInfoFromDocument(ddnetPage);
        }
        else {
            return STATUS_NO_RESULT;
        }
    }

    private int selectInfoFromDocument(@NonNull Document ddnetPage) {
        Matcher matcher;
        Elements element;

        // get ranks and points
        element = ddnetPage.select(".ladder:nth-child(1) .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            totalPointsRank = matcher.group(1);
            totalPoints = matcher.group(2);
        } else return STATUS_REGEX_ERROR_t;


        // get the first finish record
        element = ddnetPage.select(".personal-result");
        firstFinish = element.text();

        // get the newest finish record
        element = ddnetPage.select(".block7+ .ladder tr:nth-child(1) td");
        lastFinish = element.text();

        // get the monthly points ranking
        element = ddnetPage.select("#global br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            monthlyRank = matcher.group(1);
            monthlyPoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            monthlyRank = "Unranked";
            monthlyPoints = "0";
        }
        else return STATUS_REGEX_ERROR_m;

        // get the weekly points ranking
        element = ddnetPage.select("#global .ladder:nth-child(6) .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            weeklyRank = matcher.group(1);
            weeklyPoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            weeklyRank = "Unranked";
            weeklyPoints = "0";
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
            noviceRank = matcher.group(1);
            novicePoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            noviceRank = "Unranked";
            novicePoints = "0";
        }
        else return STATUS_REGEX_ERROR_n;

        // Moderate maps "rank and points"
        element = ddnetPage.select("#Moderate br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            moderateRank = matcher.group(1);
            moderatePoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            moderateRank = "Unranked";
            moderatePoints = "0";
        }
        else return STATUS_REGEX_ERROR_mo;

        // Brutal maps "rank and points"
        element = ddnetPage.select("#Brutal br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            brutalRank = matcher.group(1);
            brutalPoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            brutalRank = "Unranked";
            brutalPoints = "0";
        }
        else return STATUS_REGEX_ERROR_br;

        // Insane maps "rank and points"
        element = ddnetPage.select("#Insane br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            insaneRank = matcher.group(1);
            insanePoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            insaneRank = "Unranked";
            insanePoints = "0";
        }
        else return STATUS_REGEX_ERROR_in;

        // Dummy maps "rank and points"
        element = ddnetPage.select("#Dummy br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            dummyRank = matcher.group(1);
            dummyPoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            dummyRank = "Unranked";
            dummyPoints = "0";
        }
        else return STATUS_REGEX_ERROR_dm;

        // DDmaX maps "rank and points"
        element = ddnetPage.select("#DDmaX br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            ddmaxRank = matcher.group(1);
            ddmaxPoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            ddmaxRank = "Unranked";
            ddmaxPoints = "0";
        }
        else return STATUS_REGEX_ERROR_dx;

        // Oldschool maps "rank and points"
        element = ddnetPage.select("#Oldschool br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            oldschoolRank = matcher.group(1);
            oldschoolPoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            oldschoolRank = "Unranked";
            oldschoolPoints = "0";
        }
        else return STATUS_REGEX_ERROR_ol;

        // Solo maps "rank and points"
        element = ddnetPage.select("#Solo br+ .ladder .pers-result");
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            soloRank = matcher.group(1);
            soloPoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            soloRank = "Unranked";
            soloPoints = "0";
        }else return STATUS_REGEX_ERROR_so;

        // Race maps "rank and points"
        matcher = rpPattern.matcher(element.text());
        if (matcher.find()) {
            raceRank = matcher.group(1);
            racePoints = matcher.group(2);
        }
        else if (element.text().equals("Unranked")) {
            raceRank = "Unranked";
            racePoints = "0";
        }
        else return STATUS_REGEX_ERROR_ra;

        return STATUS_SUCCESS;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getTotalPointsRank() {
        return totalPointsRank;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public String getLastFinish() {
        return lastFinish;
    }

    public String getFirstFinish() {
        return firstFinish;
    }

    public String getMonthlyPoints() {
        return monthlyPoints;
    }

    public String getMonthlyRank() {
        return monthlyRank;
    }

    public String getWeeklyPoints() {
        return weeklyPoints;
    }

    public String getWeeklyRank() {
        return weeklyRank;
    }

    public String getNovicePoints() {
        return novicePoints;
    }

    public String getNoviceRank() {
        return noviceRank;
    }

    public String getModeratePoints() {
        return moderatePoints;
    }

    public String getModerateRank() {
        return moderateRank;
    }

    public String getBrutalPoints() {
        return brutalPoints;
    }

    public String getBrutalRank() {
        return brutalRank;
    }

    public String getInsanePoints() {
        return insanePoints;
    }

    public String getInsaneRank() {
        return insaneRank;
    }

    public String getDummyPoints() {
        return dummyPoints;
    }

    public String getDummyRank() {
        return dummyRank;
    }

    public String getDdmaxPoints() {
        return ddmaxPoints;
    }

    public String getDdmaxRank() {
        return ddmaxRank;
    }

    public String getSoloPoints() {
        return soloPoints;
    }

    public String getSoloRank() {
        return soloRank;
    }

    public String getOldschoolPoints() {
        return oldschoolPoints;
    }

    public String getOldschoolRank() {
        return oldschoolRank;
    }

    public String getRacePoints() {
        return racePoints;
    }

    public String getRaceRank() {
        return raceRank;
    }
}
