package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMoveCalculator extends PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(diagonalMovement(board, myPosition));
        moves.addAll(linearMovement(board, myPosition));
        return moves;
    }
}
