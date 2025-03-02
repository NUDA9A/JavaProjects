package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OlympGame {

    private List<Player> players;
    private final List<Player> results = new ArrayList<>();
    private int currPlayersAmount;
    private final int m;
    private final int n;
    private final int k;

    private List<Player> preTournamentPlayers = new ArrayList<>();

    private final List<Integer> preTournamentLosers = new ArrayList<>();

    private int oddPlayers = -1;
    private int preRound = 1, round = 1, player1, player2;

    private boolean preTournament = false;

    public OlympGame(int m, int n, int k, List<Player> players) { // :NOTE: Board + Players
        this.m = m;
        this.n = n;
        this.k = k;
        this.players = players;
        if (!isPower2(players.size())) {
            this.preTournament = true;
            playPreTournament();
        }
        this.currPlayersAmount = this.players.size();
        this.player1 = 0;
        this.player2 = currPlayersAmount - 1;
    }

    private boolean isPower2(int n) {
        boolean res = false;
        int i = 2;
        while (i <= n) {
            if (i == n) {
                res = true;
                this.oddPlayers = 0;
                break;
            }
            i *= 2;
        }
        if (this.oddPlayers == -1) {
            this.oddPlayers = n - (i / 2);
        }
        return res;
    }

    private void playPreTournament() {
        preTournamentPlayers.addAll(players);
        System.out.println("Starting pre-tournament.");
        while (oddPlayers != 0) {
            playPreRound();
        }
        List<Player> temp = new ArrayList<>();
        for (Player player : players) {
            if (!preTournamentLosers.contains(player.getNumber())) {
                temp.add(player);
            }
        }
        players = new ArrayList<>();
        players.addAll(temp);
    }

    private void playPreRound() {
        List<Player> nextRound = new ArrayList<>();
        int gameNum = 1;
        System.out.println("Pre-Tournament Round " + preRound++);
        int[] res = new int[preTournamentPlayers.size()];
        for (int i = 0; i < preTournamentPlayers.size() - 1; i++) {
            for (int j = i + 1; j < preTournamentPlayers.size(); j++) {
                Player player_1 = preTournamentPlayers.get(i);
                Player player_2 = preTournamentPlayers.get(j);
                int pl1 = player_1.getNumber();
                int pl2 = player_2.getNumber();
                int pl1Indx = i;
                int pl2Indx = j;
                System.out.println("Pre-Tournament Game #" + (gameNum++) + " between " + pl1 + " and " + pl2 + " players.");
                int r = new Random().nextInt(2);
                if (r == 1) {
                    Player temp = player_2;
                    player_2 = player_1;
                    player_1 = temp;
                    int numTemp = pl2;
                    pl2 = pl1;
                    pl1 = numTemp;
                    int indxTemp = pl2Indx;
                    pl2Indx = pl1Indx;
                    pl1Indx = indxTemp;
                }
                System.out.println("Player " + pl1 + " makes 1st move.");
                Game game = new Game(false, player_1, player_2);
                int result;
                do {
                    result = game.play(new MNKBoard(m, n, k));
                    if (result == 0) {
                        System.out.println("Draw");
                    }
                } while (result == 0);
                if (result == 1) {
                    System.out.println("Winner: " + "player #" + pl1);
                    res[pl1Indx]++;
                } else {
                    System.out.println("Winner: " + "player #" + pl2);
                    res[pl2Indx]++;
                }
                System.out.println();
            }
        }
        int minimum = res[0];
        for (int points : res) {
            if (points < minimum) {
                minimum = points;
            }
        }
        int countMin = 0;
        for (int points : res) {
            if (points == minimum) {
                countMin++;
            }
        }
        if (oddPlayers > countMin) {
            oddPlayers -= countMin;
            for (int i = 0; i < preTournamentPlayers.size(); i++) {
                if (res[i] != minimum) {
                    nextRound.add(preTournamentPlayers.get(i));
                } else {
                    preTournamentLosers.add(preTournamentPlayers.get(i).getNumber());
                    System.out.println("Player " + preTournamentPlayers.get(i).getNumber() + " leaves tournament.");
                }
            }
        } else if (oddPlayers == countMin) {
            oddPlayers = 0;
            for (int i = 0; i < preTournamentPlayers.size(); i++) {
                if (res[i] == minimum) {
                    preTournamentLosers.add(preTournamentPlayers.get(i).getNumber());
                    System.out.println("Player " + preTournamentPlayers.get(i).getNumber() + " leaves tournament.");
                }
            }
        } else {
            for (int i = 0; i < preTournamentPlayers.size(); i++) {
                if (res[i] == minimum) {
                    nextRound.add(preTournamentPlayers.get(i));
                }
            }
        }
        preTournamentPlayers = nextRound;
    }

    public void play() {
        System.out.println();
        System.out.println("Starting tournament.");
        while (currPlayersAmount != 1) {
            playRound();
        }
        System.out.println("Tournament results:");
        System.out.println("Winner: " + "player #" + results.get(results.size() - 1).getNumber());
        System.out.println("2nd Place: " + "player #" + results.get(results.size() - 2).getNumber());
        int k = 1;
        int place = 3;
        for (int i = results.size() - 3; i >= 0; i -= k) {
            k *= 2;
            System.out.print((place++) + " Place: ");
            printPlayers(k, i);
            System.out.println();
        }
        if (preTournament) {
            System.out.print("Lost at Pre-Tournament stage: ");
            if (preTournamentLosers.size() == 1) {
                System.out.print("player #" + preTournamentLosers.get(0));
            } else {
                System.out.print("players ");
                for (int num : preTournamentLosers) {
                    System.out.print("#" + num + " ");
                }
            }
        }
    }

    private void printPlayers(int k, int i) {
        System.out.print("players ");
        for (int j = 0; j < k; j++) {
            System.out.print("#" + results.get(i).getNumber() + " ");
            if (i - 1 >= 0) {
                i--;
            } else {
                break;
            }
        }
    }

    private void playRound() {
        List<Player> nextRound = new ArrayList<>();
        System.out.println("Round " + round++);
        int games = (int) (Math.log(currPlayersAmount) / Math.log(2));
        currPlayersAmount /= 2;
        for (int i = 0; i < currPlayersAmount; i++) {
            Player player_1 = players.get(player1++);
            Player player_2 = players.get(player2--);
            int pl1 = player_1.getNumber();
            int pl2 = player_2.getNumber();
            System.out.println("Game #" + (i + 1) + " between " + pl1 + " and " + pl2 + " players.");
            int r = new Random().nextInt(2);
            if (r == 1) {
                Player temp = player_2;
                player_2 = player_1;
                player_1 = temp;
                int indxTemp = pl2;
                pl2 = pl1;
                pl1 = indxTemp;
            }
            System.out.println("Player " + pl1 + " makes 1st move.");
            Game game = new Game(false, player_1, player_2);
            int result;
            do {
                result = game.play(new MNKBoard(m, n, k));
                if (result == 0) {
                    System.out.println("Draw");
                }
            } while (result == 0);
            if (result == 1) {
                System.out.println("Winner: " + "player #" + pl1);
                nextRound.add(player_1);
                System.out.println(pl2 + " player takes " + (games + 1) + " place.");
                results.add(player_2);
                if (currPlayersAmount == 1) {
                    results.add(player_1);
                }
            } else {
                System.out.println("Winner: " + "player #" + pl2);
                nextRound.add(player_2);
                System.out.println(pl1 + " player takes " + (games + 1) + " place.");
                results.add(player_1);
                if (currPlayersAmount == 1) {
                    results.add(player_2);
                }
            }
            System.out.println();
        }
        players = nextRound;
        player1 = 0;
        player2 = players.size() - 1;
    }
}
