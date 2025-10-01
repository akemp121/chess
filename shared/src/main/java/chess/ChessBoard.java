package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public Collection<ChessMove> getTeamMoves(ChessGame.TeamColor color) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition curr = new ChessPosition(i, j);
                if (getPiece(curr) != null) {
                    if (getPiece(curr).getTeamColor() == color) {
                        moves.addAll(getPiece(curr).pieceMoves(this, curr));
                    }
                }
            }
        }
        return moves;
    }

    public ChessPosition getKingPos(ChessGame.TeamColor color) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition curr = new ChessPosition(i, j);
                if (getPiece(curr) != null) {
                    if (getPiece(curr).getTeamColor() == color && getPiece(curr).getPieceType() == ChessPiece.PieceType.KING) {
                        return curr;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // clear the array:
        squares = new ChessPiece[8][8];

        ChessPiece.PieceType[] ordered = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (int i = 1; i <= 8; i++) {
            // white
            addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ordered[i - 1]));
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            // black
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ordered[i - 1]));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }
}
