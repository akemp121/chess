package chess.moves;

import chess.ChessPiece;

public class CalculatorFactory {

    public static PieceMoveCalculator getCalculator(ChessPiece.PieceType type) {
        switch (type) {
            case ChessPiece.PieceType.BISHOP:
                return new BishopMoveCalculator();
            case ChessPiece.PieceType.KING:
                return new KingMoveCalculator();
            case ChessPiece.PieceType.KNIGHT:
                return new KnightMoveCalculator();
            case ChessPiece.PieceType.PAWN:
                return new PawnMoveCalculator();
            case ChessPiece.PieceType.QUEEN:
                return new QueenMoveCalculator();
            case ChessPiece.PieceType.ROOK:
                return new RookMoveCalculator();
        }
        return null;
    }

}
