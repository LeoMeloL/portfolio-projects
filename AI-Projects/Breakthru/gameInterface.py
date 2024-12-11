import tkinter as tk

class GameInterface(tk.Tk):
    def __init__(self, ai_player, player_choice):
        super().__init__()
        self.title("Seu Jogo")
        self.player_choice = player_choice
        self.ai_player = ai_player
        from noInterface2 import rawGame
        self.game = rawGame(player_choice, ai_player)
        self.canvas = tk.Canvas(self, width=400, height=400)
        self.board_matrix = self.game.get_game_matrix()
        self.canvas.pack()
        self.draw_board()
        self.selected_piece = None
        self.canvas.bind("<Button-1>", self.on_click)

    def draw_board(self):
        self.canvas.delete("all")
        cell_size = 50
        colors = {1: "red", 2: "blue", 3: "green"}
        for row in range(len(self.board_matrix)):
            for col in range(len(self.board_matrix[row])):
                x0 = col * cell_size
                y0 = row * cell_size
                x1 = x0 + cell_size
                y1 = y0 + cell_size
                piece = self.board_matrix[row][col]
                color = colors.get(piece, "white")
                self.canvas.create_rectangle(x0, y0, x1, y1, fill=color, outline="black")

    def update_board(self, new_board_matrix):
        self.board_matrix = new_board_matrix
        self.draw_board()
        
    def on_click(self, event):
        col = event.x // 50
        row = event.y // 50
        piece = self.board_matrix[row][col]
        if piece == self.player_choice:
            if self.selected_piece is None:
                self.selected_piece = (row, col)
            else:
                new_row, new_col = row, col
                old_row, old_col = self.selected_piece
                if self.game.is_valid_move(old_row, old_col, new_row, new_col):
                    self.game.move_piece(old_row, old_col, new_row, new_col)
                    self.update_board()
                self.selected_piece = None


# Exemplo de uso:
if __name__ == "__main__":
    board_matrix = [[0] * 7 for _ in range(7)]
    for piece in {(2, 0), (3, 0), (4, 0), (6, 2), (6, 3), (6, 4), (0, 2), (0, 3), (0, 4), (2, 6), (3, 6), (4, 6)}:
        row, col = piece
        board_matrix[col][row] = 1
    for piece in {(2, 2), (2, 3), (2, 4), (4, 2), (4, 3), (4, 4), (3, 2), (3, 4)}:
        row, col = piece
        board_matrix[col][row] = 2
    for flag in {(3, 3)}:
        row, col = flag
        board_matrix[col][row] = 3

    game_interface = GameInterface(2,1)
    # Aqui você pode atualizar o tabuleiro chamando game_interface.update_board(new_board_matrix)
    # com a nova matriz do tabuleiro
    game_interface.mainloop()
