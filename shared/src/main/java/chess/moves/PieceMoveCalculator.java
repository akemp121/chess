package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public abstract class PieceMoveCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
