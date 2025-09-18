package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMoveCalculator extends PieceMoveCalculator {

    public ChessMove knightMoves(ChessPiece piece, ChessBoard board, ChessPosition myPosition, int x, int y) {
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

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] possibleMovement = {
                {-2, -1}, {-2, 1}, {-1, 2}, {-1, -2}, {2, -1}, {2, 1}, {1, -2}, {1, 2}
        };
        for (int[] i : possibleMovement) {
            ChessMove toAdd = knightMoves(piece, board, myPosition, i[0], i[1]);
            if (toAdd != null) {
                moves.add(toAdd);
            }
        }
        return moves;
    }
}
