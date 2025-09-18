package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMoveCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public Collection<ChessMove> diagonalMovement(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition curr = myPosition;
        // up-left
        while (curr.getRow() > 1 && curr.getColumn() > 1) {
            ChessPosition new_pos = new ChessPosition(curr.getRow() - 1, curr.getColumn() - 1);
            curr = new_pos;
            // if the space is empty
            if (board.getPiece(curr) == null) {
                moves.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        // up-right
        curr = myPosition;
        while (curr.getRow() > 1 && curr.getColumn() < 8) {
            ChessPosition new_pos = new ChessPosition(curr.getRow() - 1, curr.getColumn() + 1);
            curr = new_pos;
            // if the space is empty
            if (board.getPiece(curr) == null) {
                moves.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        // down-left
        curr = myPosition;
        while (curr.getRow() < 8 && curr.getColumn() > 1) {
            ChessPosition new_pos = new ChessPosition(curr.getRow() + 1, curr.getColumn() - 1);
            curr = new_pos;
            // if the space is empty
            if (board.getPiece(curr) == null) {
                moves.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        // down-right
        curr = myPosition;
        while (curr.getRow() < 8 && curr.getColumn() < 8) {
            ChessPosition new_pos = new ChessPosition(curr.getRow() + 1, curr.getColumn() + 1);
            curr = new_pos;
            // if the space is empty
            if (board.getPiece(curr) == null) {
                moves.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    public Collection<ChessMove> linearMovement(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition curr = myPosition;
        // up
        while (curr.getRow() > 1) {
            ChessPosition new_pos = new ChessPosition(curr.getRow() - 1, curr.getColumn());
            curr = new_pos;
            // if the space is empty
            if (board.getPiece(curr) == null) {
                moves.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        // down
        curr = myPosition;
        while (curr.getRow() < 8) {
            ChessPosition new_pos = new ChessPosition(curr.getRow() + 1, curr.getColumn());
            curr = new_pos;
            // if the space is empty
            if (board.getPiece(curr) == null) {
                moves.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        // left
        curr = myPosition;
        while (curr.getColumn() > 1) {
            ChessPosition new_pos = new ChessPosition(curr.getRow(), curr.getColumn() - 1);
            curr = new_pos;
            // if the space is empty
            if (board.getPiece(curr) == null) {
                moves.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        // right
        curr = myPosition;
        while (curr.getColumn() < 8) {
            ChessPosition new_pos = new ChessPosition(curr.getRow(), curr.getColumn() + 1);
            curr = new_pos;
            // if the space is empty
            if (board.getPiece(curr) == null) {
                moves.add(new ChessMove(myPosition, curr, null));
            } else if (board.getPiece(curr).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, curr, null));
                break;
            } else {
                break;
            }
        }
        return moves;
    }


}
