package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends PieceMoveCalculator {



    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] possibleMovement = {
                {-2, -1}, {-2, 1}, {-1, 2}, {-1, -2}, {2, -1}, {2, 1}, {1, -2}, {1, 2}
        };
        for (int[] i : possibleMovement) {
            ChessMove toAdd = singularMove(piece, board, myPosition, i[0], i[1]);
            if (toAdd != null) {
                moves.add(toAdd);
            }
        }
        return moves;
    }
}
