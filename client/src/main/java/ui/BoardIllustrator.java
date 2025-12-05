package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;
import chess.*;
import chess.ChessPiece.PieceType;

public class BoardIllustrator {

    public static void illustrate(ChessBoard board, ChessGame.TeamColor teamColor) {
        // depending on the team color, direction of loop will change
        // loop through each row
            // loop through each column
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessPiece[][] squares = board.getSquares();

        out.print(ERASE_SCREEN);

        printTopBottom(out, teamColor);
        out.print("\n");
        // print board
        if (teamColor.toString().equals("BLACK")) {
            for (int i = 0; i < 8; i++) {
                printRow(out, teamColor, squares[i], 7 - i);
                out.print(SET_BG_COLOR_BLACK);
                out.print("\n");
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                printRow(out, teamColor, squares[i], 7 - i);
                out.print(SET_BG_COLOR_BLACK);
                out.print("\n");
            }
        }

        printTopBottom(out, teamColor);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print("\n");
    }

    public static void higlightMoves(ChessBoard board, ChessGame.TeamColor teamColor, ChessPosition position) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessPiece[][] squares = board.getSquares();

        Collection<ChessMove> moves = board.getPiece(position).pieceMoves(board, position);

        out.print(ERASE_SCREEN);

        printTopBottom(out, teamColor);
        out.print("\n");

        // print HIGHLIGHTED board
        if (teamColor.toString().equals("BLACK")) {
            for (int i = 0; i < 8; i++) {
                printHighlightedRow(out, teamColor, squares[i], 7 - i, getEndPositions(moves));
                out.print(SET_BG_COLOR_BLACK);
                out.print("\n");
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                printHighlightedRow(out, teamColor, squares[i], 7 - i, getEndPositions(moves));
                out.print(SET_BG_COLOR_BLACK);
                out.print("\n");
            }
        }
        printTopBottom(out, teamColor);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print("\n");

    }

    private static Collection<ChessPosition> getEndPositions(Collection<ChessMove> moves) {
        Collection<ChessPosition> positions = new ArrayList<>();
        for (ChessMove m : moves) {
            positions.add(m.getEndPosition());
        }
        return positions;
    }

    private static void printTopBottom(PrintStream out, ChessGame.TeamColor teamColor) {
        String[] key = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        printSpace(out);
        if (teamColor.toString().equals("WHITE")) {
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

    private static void printRow(PrintStream out, ChessGame.TeamColor teamColor, ChessPiece[] row, int rowNum) {
        printSpaceWithNumber(out, 8 - rowNum);
        if (teamColor.toString().equals("WHITE")) {
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

    private static void printHighlightedRow(PrintStream out, ChessGame.TeamColor teamColor,
                                            ChessPiece[] row, int rowNum, Collection<ChessPosition> positions) {
        printSpaceWithNumber(out, 8 - rowNum);
        if (teamColor.toString().equals("WHITE")) {
            for (int i = 0; i < 8; i++) {
                printHighlightedLine(i, rowNum, row, out, positions);
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                printHighlightedLine(i, rowNum, row, out, positions);
            }
        }
        printSpaceWithNumber(out, 8 - rowNum);
    }

    private static void printHighlightedLine(int i, int rowNum, ChessPiece[] row, PrintStream out,
                                             Collection<ChessPosition> positions) {

        // alternating colors
        ChessPosition curr = new ChessPosition(8 - rowNum, i + 1);
        if ((i + rowNum) % 2 == 0) {
            if (positions.contains(curr)) {
                out.print(SET_BG_COLOR_GREEN);
            } else {
                out.print(SET_BG_COLOR_WHITE);
            }
        } else {
            if (positions.contains(curr)) {
                out.print(SET_BG_COLOR_GREEN);
            } else {
                out.print(SET_BG_COLOR_BLACK);
            }
        }
        if (row[i] == null) {
            out.print(" ");
        } else {
            printPieceLetter(row[i], out);
        }
        // printing spaces in between
        out.print(" ");
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
