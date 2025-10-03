package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard game_board = new ChessBoard();
    TeamColor turn;

    public ChessGame() {
        game_board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public void makeMoveGeneric(ChessMove move, ChessBoard board) {
        // get current piece
        ChessPiece curr = board.getPiece(move.getStartPosition());
        ChessPiece.PieceType startType = curr.getPieceType();
        TeamColor startColor = curr.getTeamColor();
        // make startingPosition null
        board.removePiece(move.getStartPosition());
        // if promotionPiece
        if (move.getPromotionPiece() != null) {
            // put the promotion piece in the new position
            board.addPiece(move.getEndPosition(), new ChessPiece(startColor, move.getPromotionPiece()));
        }
        // if not
        else { // put the same piece in the new position
            board.addPiece(move.getEndPosition(), new ChessPiece(startColor, startType));
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    // basically get pieceMoves and delete the ones that put the king in check
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // get the piecemoves
        ChessPiece curr = game_board.getPiece(startPosition);
        Collection<ChessMove> moves = curr.pieceMoves(game_board, startPosition);
        Collection<ChessMove> valid = new ArrayList<>();
        // create a clone of the current board
        for (ChessMove move : moves) {
            ChessBoard copy = (ChessBoard) game_board.clone();
            // make the move
            makeMoveGeneric(move, copy);
            if (!checkCheck(curr.getTeamColor(), copy)) {
                // delete the move
                valid.add(move);
            }
        }
        return valid;
    }

    public Collection<ChessMove> getValidMoves(TeamColor teamColor) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition curr = new ChessPosition(i, j);
                ChessPiece currPiece = game_board.getPiece(curr);
                if (currPiece != null) {
                    if (currPiece.getTeamColor() == teamColor) {
                        moves.addAll(validMoves(curr));
                    }
                }
            }
        }
        return moves;
    }

    public boolean checkCheck(TeamColor teamColor, ChessBoard board) {
        TeamColor opposite = TeamColor.BLACK;
        if (teamColor == TeamColor.BLACK) {
            opposite = TeamColor.WHITE;
        }
        // loop through enemy's moves:
        ChessPosition kingPos = board.getKingPos(teamColor);
        for (ChessMove m : board.getTeamMoves(opposite)) {
            ChessPosition enemyPos = m.getEndPosition();
            if (enemyPos.equals(kingPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    // if the move isn't in valid moves, then throw the exception
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // check if move is valid, if not, then throw the INVALID
        ChessPosition startPosition = move.getStartPosition();
        Collection<ChessMove> moves = validMoves(startPosition);
        boolean isValid = Arrays.asList(moves).contains(move);
        if (game_board.getPiece(startPosition).getTeamColor() == turn && isValid) {
            makeMoveGeneric(move, game_board);
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    // go through all the pieceMoves of the other team, if "endPosition" is on our team's king, then return true
    public boolean isInCheck(TeamColor teamColor) {
        return checkCheck(teamColor, game_board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    // NOBODY has no valid moves and KING IS IN CHECK
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> allMoves = getValidMoves(teamColor);
        if (allMoves.isEmpty() && isInCheck(teamColor)) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    // NOBODY has no valid moves and KING IS NOT IN CHECK
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> allMoves = getValidMoves(teamColor);
        if (allMoves.isEmpty() && !isInCheck(teamColor)) {
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        game_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return game_board;
    }
}
