package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import chess.*;
import chess.ChessPiece.PieceType;

public class BoardIllustrator {

    public static void illustrate(ChessBoard board, String teamColor) {
        // depending on the team color, direction of loop will change
        // loop through each row
            // loop through each column
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessPiece[][] squares = board.getSquares();

        out.print(ERASE_SCREEN);

        printTopBottom(out, teamColor);
        out.print("\n");
        // print board
        if (teamColor.equals("WHITE")) {
            for (int i = 0; i < 8; i++) {
                printRow(out, teamColor, squares[i], i);
                out.print(SET_BG_COLOR_BLACK);
                out.print("\n");
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                printRow(out, teamColor, squares[i], i);
                out.print(SET_BG_COLOR_BLACK);
                out.print("\n");
            }
        }

        printTopBottom(out, teamColor);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print("\n");
    }

    private static void printTopBottom(PrintStream out, String teamColor) {
        String[] key = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        printSpace(out);
        if (teamColor.equals("WHITE")) {
            for (int i = 0; i < 8; i++) {
                out.print(key[i]);
                out.print(" ");
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                out.print(key[i]);
                out.print(" ");
            }
        }
        printSpace(out);
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void printRow(PrintStream out, String teamColor, ChessPiece[] row, int rowNum) {
        printSpaceWithNumber(out, 8 - rowNum);
        if (teamColor.equals("WHITE")) {
            for (int i = 0; i < 8; i++) {
                printLine(i, rowNum, row, out);
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                printLine(i, rowNum, row, out);
            }
        }
        printSpaceWithNumber(out, 8 - rowNum);
    }

    private static void printLine(int i, int rowNum, ChessPiece[] row, PrintStream out) {
        // alternating colors
        if ((i + rowNum) % 2 == 0) {
            out.print(SET_BG_COLOR_WHITE);
        } else {
            out.print(SET_BG_COLOR_BLACK);
        }
        if (row[i] == null) {
            out.print(" ");
        } else {
            printPieceLetter(row[i], out);
        }
        // printing spaces in between
        out.print(" ");
    }

    private static void printSpace(PrintStream out) {
        out.print("   ");
    }

    private static void printSpaceWithNumber(PrintStream out, int num) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(" " + num + " ");
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void printPieceLetter(ChessPiece piece, PrintStream out) {
        out.print(SET_TEXT_BOLD);
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_BLUE);
        } else {
            out.print(SET_TEXT_COLOR_RED);
        }
        switch (piece.getPieceType()) {
            case PieceType.BISHOP -> out.print("B");
            case PieceType.KING -> out.print("K");
            case PieceType.ROOK -> out.print("R");
            case PieceType.PAWN -> out.print("P");
            case PieceType.QUEEN -> out.print("Q");
            case PieceType.KNIGHT -> out.print("N");
        };
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_FAINT);
    }

}
