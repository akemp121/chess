package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMoveCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public Collection<ChessMove> lineMoves(ChessPiece piece, ChessBoard board, ChessPosition myPosition, int x, int y) {
        ChessPosition curr = myPosition;
        Collection<ChessMove> toAdd = new ArrayList<>();
        // while in bounds
        while ((curr.getRow() + x) >= 1 && (curr.getRow() + x) <= 8 && (curr.getColumn() + y) >= 1 && (curr.getColumn() + y) <= 8) {
            curr = new ChessPosition(curr.getRow() + x, curr.getColumn() + y);
            // if the space is empty
            if (board.getPiece(curr) == null) {
                toAdd.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                toAdd.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        return toAdd;
    }

    public Collection<ChessMove> diagonalMovement(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] possibleDiagonal = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };
        for (int[] i : possibleDiagonal) {
            Collection<ChessMove> toAdd = lineMoves(piece, board, myPosition, i[0], i[1]);
            moves.addAll(toAdd);
        }
        return moves;
    }

    public Collection<ChessMove> linearMovement(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] possibleLinear = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}
        };
        for (int[] i : possibleLinear) {
            Collection<ChessMove> toAdd = lineMoves(piece, board, myPosition, i[0], i[1]);
            moves.addAll(toAdd);
        }
        return moves;
    }

    public ChessMove singularMove(ChessPiece piece, ChessBoard board, ChessPosition myPosition, int x, int y) {
        // if we're in bounds:
        if ((myPosition.getRow() + x) >= 1 && (myPosition.getRow() + x) <= 8 && (myPosition.getColumn() + y) >= 1 && (myPosition.getColumn() + y) <= 8) {
            ChessPosition curr = new ChessPosition(myPosition.getRow() + x, myPosition.getColumn() + y);
            // if it's null or occupied by enemy:
            if (board.getPiece(curr) == null || board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                return new ChessMove(myPosition, curr, null);
            }
            return null;
        }
        return null;
    }


}
