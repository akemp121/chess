package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalculator extends PieceMoveCalculator {

    public Collection<ChessMove> promotionMoves(ChessPiece piece, ChessPosition startPos, ChessPosition endPos) {
        Collection<ChessMove> promotionMoves = new ArrayList<>();
        // if we're promoting:
        if ((piece.getTeamColor() == ChessGame.TeamColor.WHITE && endPos.getRow() == 8) || (piece.getTeamColor() == ChessGame.TeamColor.BLACK && endPos.getRow() == 1)) {
            // all promotions
            ChessPiece.PieceType[] types = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
            for (ChessPiece.PieceType t : types) {
                promotionMoves.add(new ChessMove(startPos, endPos, t));
            }
        } else {
            // what we started with
            promotionMoves.add(new ChessMove(startPos, endPos, null));
        }
        return promotionMoves;
    }

    public Collection<ChessMove> straightPawn(ChessPiece piece, ChessBoard board, ChessPosition myPosition, int y) {
        Collection<ChessMove> colToAdd = new ArrayList<>();
        int j = 0;
        for (int i = 1; i <= y; i++) {
            j = i;
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {j *= -1;}
            // if we're in bounds:
            if ((myPosition.getRow() + j) >= 1 && (myPosition.getRow() + j) <= 8) {
                ChessPosition curr = new ChessPosition(myPosition.getRow() + j, myPosition.getColumn());
                // if it's null:
                if (board.getPiece(curr) == null) {
                    // if we're promoting:
                    colToAdd.addAll(promotionMoves(piece, myPosition, curr));
                } else {
                    break;
                }
            }
        }
        return colToAdd;
    }

    public Collection<ChessMove> attackPawn(ChessPiece piece, ChessBoard board, ChessPosition myPosition, int y) {
        Collection<ChessMove> colToAdd = new ArrayList<>();
        ChessPosition curr = new ChessPosition(myPosition.getRow() + y, myPosition.getColumn());
        // if in bounds:
        if ((myPosition.getRow() + y) >= 1 && (myPosition.getRow() + y) <= 8) {
            // left
            if ((curr.getColumn() - 1) >= 1) {
                curr = new ChessPosition(curr.getRow(), curr.getColumn() - 1);
                if (board.getPiece(curr) != null && board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                    colToAdd.addAll(promotionMoves(piece, myPosition, curr));
                }
            }
            // right
            if ((curr.getColumn() + 2) <= 8) {
                curr = new ChessPosition(curr.getRow(), curr.getColumn() + 2);
                if (board.getPiece(curr) != null && board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                    colToAdd.addAll(promotionMoves(piece, myPosition, curr));
                }
            }
        }
        return colToAdd;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() == 2) {
                Collection<ChessMove> toAdd = straightPawn(piece, board, myPosition, 2);
                if (toAdd != null) {
                    moves.addAll(toAdd);
                }
            } else {
                Collection<ChessMove> toAdd = straightPawn(piece, board, myPosition, 1);
                if (toAdd != null) {
                    moves.addAll(toAdd);
                }
            }
            Collection<ChessMove> toAdd = attackPawn(piece, board, myPosition, 1);
            if (toAdd != null) {
                moves.addAll(toAdd);
            }
        } else {
            if (myPosition.getRow() == 7) {
                Collection<ChessMove> toAdd = straightPawn(piece, board, myPosition, 2);
                if (toAdd != null) {
                    moves.addAll(toAdd);
                }
            } else {
                Collection<ChessMove> toAdd = straightPawn(piece, board, myPosition, 1);
                if (toAdd != null) {
                    moves.addAll(toAdd);
                }
            }
            Collection<ChessMove> toAdd = attackPawn(piece, board, myPosition, -1);
            if (toAdd != null) {
                moves.addAll(toAdd);
            }
        }
        return moves;
    }
}
