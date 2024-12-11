class rawGame:
    def __init__(self, player_choice, ai_player):
        self.numPieces = 12
        self.numPieces2 = 8

        self.board_size = 7
        self.board_matrix = [[0] * self.board_size for _ in range(self.board_size)]  # Matriz do tabuleiro
        for piece in {(2, 0), (3, 0), (4, 0), (6, 2), (6, 3), (6, 4), (0, 2), (0, 3), (0, 4), (2, 6), (3, 6), (4, 6)}:
            row, col = piece
            self.board_matrix[col][row] = 1
        for piece in {(2, 2), (2, 3), (2, 4), (4, 2), (4, 3), (4, 4), (3, 2), (3, 4)}:
            row, col = piece
            self.board_matrix[col][row] = 2
        for flag in {(3, 3)}:
            row, col = flag
            self.board_matrix[col][row] = 3

        self.selected_piece = None
        self.selected_piece2 = None
        self.selected_flag = None
        self.player = player_choice

        self.ai_player = ai_player
        
    def evaluate_board_complete(self, player): #Funcao heuristica completa focando na melhor jogada entre todas as estratégias
        weight_pieces = 1.0
        weight_flag = 1500.0
        weight_defense = 300

        player1_pieces = self.numPieces
        player2_pieces = self.numPieces2

        player1_score = weight_pieces * player1_pieces
        player2_score = weight_pieces * player2_pieces

        x, y = self.find_flag_position()

        # Verifica se a bandeira está em perigo imediato
        if self.is_flag_imminent_danger(x, y):
            return player2_score - player1_score + weight_flag * 2

        # Verifica se alguma peça do jogador 2 está ameaçada
        pieces_threatened = self.are_pieces_threatened(player)

        # Se a bandeira estiver ameaçada, priorize a defesa
        if self.is_flag_threatened(x, y):
            return player2_score - player1_score + weight_flag

        # Se alguma peça estiver ameaçada, priorize a defesa da peça que também ameaça a bandeira
        if pieces_threatened:
            for piece in pieces_threatened:
                if self.is_piece_threatening_flag(piece, x, y):
                    return player2_score - player1_score + weight_defense * 2
            return player2_score - player1_score + weight_defense

        # Avaliação defensiva, ofensiva e de conquista
        flag_score = self.evaluate_board_evasive(self, player)
        conquer_score = self.evaluate_board_conqueror(self, player)

        return player2_score - player1_score + flag_score + conquer_score

    
    def evaluate_board_evasive(self, player):   #Funcao de avaliacao focada na fuga com a bandeira e defesa
        weight_corner = 100
        weight_adjacent_enemy = -20

        x, y = self.find_flag_position(player)

        # Pontuação da bandeira
        distance_to_corner = min(x, y, self.board_size - x - 1, self.board_size - y - 1)
        flag_score = weight_corner - distance_to_corner

        # Pontuação extra para cantos
        if x == 0 and y == 0:
            flag_score += 50
        elif x == 0 and y == self.board_size - 1:
            flag_score += 50
        elif x == self.board_size - 1 and y == 0:
            flag_score += 50
        elif x == self.board_size - 1 and y == self.board_size - 1:
            flag_score += 50

        # Penalização por peças inimigas adjacentes
        for dx in range(-1, 2):
            for dy in range(-1, 2):
                if 0 <= x + dx < self.board_size and 0 <= y + dy < self.board_size:
                    if self.board_matrix[y + dy][x + dx] == 1:
                        flag_score += weight_adjacent_enemy

        return flag_score
    
    
    def evaluate_board_conqueror(self, player): #Funcao heuristica focada em dominar o tabuleiro
        weight_pieces = 1.0

        player1_pieces = self.numPieces
        player2_pieces = self.numPieces2
 
        # Penalização por peças do oponente
        opponent_pieces = player2_pieces if player == 1 else player1_pieces
        penalty_opponent_pieces = weight_pieces * opponent_pieces

        # Pontuação por peças colocadas
        score = weight_pieces * player1_pieces

        return score - penalty_opponent_pieces
    
    def is_flag_imminent_danger(self, x, y): #Verifica se a bandeira está em perigo iminente

        for dx in range(-1, 2):
            for dy in range(-1, 2):
                if 0 <= x + dx < self.board_size and 0 <= y + dy < self.board_size:
                    if self.board_matrix[y + dy][x + dx] == 1:
                        return True

        return False



    
    def is_piece_threatening_flag(self, piece, x, y): #Verifica se tem alguma peca ameacando a bandeira
        piece_row, piece_col = piece

        # Verifica se a peça está na mesma linha ou coluna da bandeira
        if piece_row == x or piece_col == y:
            return True

        # Verifica se a peça está na diagonal da bandeira
        if abs(piece_row - x) == abs(piece_col - y):
            return True

        return False
    
    
    def get_distance(self, row1, col1, row2, col2): #Pega a distancia de uma peca até a bandeira
        
        distance_x = abs(row1 - row2)
        distance_y = abs(col1 - col2)
        return distance_x + distance_y



    def are_pieces_threatened(self, player): #Verifica quais as pecas que estao sendo ameacadas
        threatened_pieces = []
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self.board_matrix[row][col] == player:
                    for d_row in range(-1, 2):
                        for d_col in range(-1, 2):
                            if (d_row != 0 or d_col != 0) and 0 <= row + d_row < self.board_size and 0 <= col + d_col < self.board_size:
                                if self.board_matrix[row + d_row][col + d_col] == 1:
                                    threatened_pieces.append((row, col))
        return threatened_pieces



    def find_flag_position(self): # Encontra a posição da bandeira no tabuleiro
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self.board_matrix[row][col] == 3:  # Flag
                    #print("achou")
                    return row, col
        return -1, -1
    
    def is_flag_dead(self): #Verifica se a bandeira morreu
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self.board_matrix[row][col] == 3:
                    return False
                
        return True
        
    def is_flag_threatened(self, flag_row, flag_col): # Verifica se a bandeira está ameaçada por uma peça do jogador 1 nas diagonais
        diagonals = [(-1, -1), (-1, 1), (1, -1), (1, 1)]
        for d_row, d_col in diagonals:
            row = flag_row + d_row
            col = flag_col + d_col
            if 0 <= row < len(self.board_matrix) and 0 <= col < len(self.board_matrix[row]):
                if self.board_matrix[row][col] == 1:
                    print("A bandeira está ameaçada por uma peça do jogador 1!")
                    return True
        return False



    def verificar_bandeira_nos_cantos(self): #Funcao para verificar se a bandeira fugiu
        corners = [(0, 0), (0, 6), (6, 0), (6, 6), (6,5), (6,4), (6,3), (6,2), (6,1), (1,6), (2,6), (3,6), (4,6), (5,6)] 
        flag_position = next(((i, j) for i in range(self.board_size) for j in range(self.board_size) if self.board_matrix[i][j] == 3), None)

        if flag_position in corners:
            print("A bandeira está em um dos cantos do tabuleiro!")
            print("Jogo acabou")
            exit(0)
            
    def is_valid_move(self, origin, dest, player): #Verifica se o movimento é valido
        opponent = 2 if player == 1 else 1
        
        # Movimento lateral
        if (dest[0] == origin[0] and abs(dest[1] - origin[1]) == 1):
            if self.board_matrix[dest[1]][dest[0]] == 0:
                return True
        
        # Movimento vertical
        elif (dest[1] == origin[1] and abs(dest[0] - origin[0]) == 1):
            if self.board_matrix[dest[1]][dest[0]] == 0:
                return True
        
        # Movimento diagonal para comer uma peça do oponente
        elif (self.board_matrix[dest[1]][dest[0]] == opponent) and abs(dest[0] - origin[0]) == 1 and abs(dest[1] - origin[1]) == 1:
            return True

        # Movimento diagonal para comer a bandeira
        elif (self.board_matrix[dest[1]][dest[0]] == 3) and abs(dest[0] - origin[0]) == 1 and abs(dest[1] - origin[1]) == 1 and player != 2:
            return True
        return False



    
    def generate_moves(self, player): #Gera uma lista com todos os movimentos possiveis para determinado jogador
            moves = []
            for row in range(self.board_size):
                for col in range(self.board_size):
                    if player == 1 and self.board_matrix[col][row] == 1:
                        for d_row in range(-1, 2):
                            for d_col in range(-1, 2):
                                if (d_row != 0 or d_col != 0) and 0 <= row + d_row < self.board_size and 0 <= col + d_col < self.board_size:
                                    dest = (row + d_row, col + d_col)
                                    if self.is_valid_move((row, col), dest, player):
                                        moves.append((row, col, dest))
                    elif player == 2 and self.board_matrix[col][row] == 2:
                        for d_row in range(-1, 2):
                            for d_col in range(-1, 2):
                                if (d_row != 0 or d_col != 0) and 0 <= row + d_row < self.board_size and 0 <= col + d_col < self.board_size:
                                    dest = (row + d_row, col + d_col)
                                    if self.is_valid_move((row, col), dest, player):
                                        moves.append((row, col, dest))
                                        
                    elif player == 2 and self.board_matrix[col][row] == 3:
                        for d_row in range(-1, 2):
                            for d_col in range(-1, 2):
                                if (d_row != 0 or d_col != 0) and 0 <= row + d_row < self.board_size and 0 <= col + d_col < self.board_size:
                                    dest = (row + d_row, col + d_col)
                                    if self.is_valid_move((row, col), dest, player):
                                        moves.append((row, col, dest))  
            return moves


    def game(self):
        current_player = self.player
        other_player = self.ai_player
        alpha = -float('inf')
        beta = float('inf')
        while True:
            print("Tabuleiro atual:")
            for row in self.board_matrix:
                print(row)

            if current_player == 1 and self.find_flag_position() == -1:
                print("Parabéns! Jogador 1 venceu!")
                break
            if self.is_flag_dead():
                print("Bandeira eliminada")
                break

            if not self.generate_moves(current_player):
                print(f"Jogador {current_player} não tem movimentos válidos. O outro jogador vence!")
                break

            if current_player == self.player:  # Se o jogador atual for o mesmo que self.player, é a vez do jogador humano
                print(f"Jogador {current_player}, é sua vez.")
                while True:
                    try:
                        x, y = map(int, input("Digite as coordenadas x e y da peça que deseja mover (separadas por espaço): ").split())
                        if self.board_matrix[y][x] == current_player:
                            break
                        else:
                            print("Coordenadas inválidas. Por favor, escolha uma peça sua.")
                    except ValueError:
                        print("Entrada inválida. Por favor, digite duas coordenadas separadas por espaço.")

                while True:
                    try:
                        x_dest, y_dest = map(int, input("Digite as coordenadas x e y para onde deseja mover a peça (separadas por espaço): ").split())
                        if self.is_valid_move((x, y), (x_dest, y_dest), current_player):
                            break
                        else:
                            print("Movimento inválido. Por favor, escolha uma posição válida.")
                    except ValueError:
                        print("Entrada inválida. Por favor, digite duas coordenadas separadas por espaço.")

                self.board_matrix[y][x] = 0
                if self.board_matrix[y_dest][x_dest] == other_player:
                    if other_player == 1:
                        self.numPieces -= 1
                    else:
                        self.numPieces2 -= 1
                
                self.board_matrix[y_dest][x_dest] = current_player

            else:  # Caso contrário, é a vez da IA
                aux = True if self.ai_player == 2 else False
                _, best_move = self.minimax(3, alpha, beta, aux)
                print(best_move)
                if best_move:
                    x, y, dest = best_move
                    x_dest, y_dest = dest
                    old_x, old_y = x, y
                    if self.board_matrix[y_dest][x_dest] == self.player:
                        if self.player == 1:
                            self.numPieces -= 1
                            print(self.numPieces)
                        else:
                            self.numPieces2 -= 1
                            print(self.numPieces2)
                    if self.board_matrix[old_y][old_x] == 3:
                        self.board_matrix[y_dest][x_dest] = 3
                    else:
                        self.board_matrix[y_dest][x_dest] = current_player
                            
                    self.board_matrix[y][x] == 0
                    self.last_move = (x, y, dest)

            self.verificar_bandeira_nos_cantos()
            current_player = 2 if current_player == 1 else 1  # Alterna para o próximo jogador
            other_player = 1 if current_player == 2 else 2
            self.board_matrix[y][x] = 0


    def minimax(self, depth, alpha, beta, maximizing_player): #Funcao minimax com poda alpha-beta e bonus de captura
        current_player = self.ai_player
        if depth == 0:
            return self.evaluate_board_complete(maximizing_player), None

        if maximizing_player:
            max_eval = float('-inf')
            best_move = None
            for move in self.generate_moves(current_player):
                x, y, dest = move
                capture_bonus = 0  # Bonus for capturing enemy piece

                if self.board_matrix[dest[1]][dest[0]] == 3:  # Capture enemy flag
                    capture_bonus = 1000  # High bonus for capturing flag

                elif self.board_matrix[dest[1]][dest[0]] == 1:  # Capture enemy piece
                    capture_bonus = 50  # Bonus for capturing piece

                temp_origin = self.board_matrix[y][x]
                temp_dest = self.board_matrix[dest[1]][dest[0]]
                self.board_matrix[y][x] = 0
                self.board_matrix[dest[1]][dest[0]] = current_player

                eval, _ = self.minimax(depth - 1, alpha, beta, True)
                self.board_matrix[dest[1]][dest[0]] = temp_dest
                self.board_matrix[y][x] = temp_origin

                # Add capture bonus to evaluation score
                eval += capture_bonus

                if eval > max_eval:
                    max_eval = eval
                    best_move = move

                # Prune if the current evaluation is worse than the beta value for the minimizing player
                if eval >= beta:
                    break

            return max_eval, best_move
        else:
            min_eval = float('inf')
            best_move = None
            for move in self.generate_moves(current_player):
                x, y, dest = move
                if self.board_matrix[y][x] == current_player:
                    temp_origin = self.board_matrix[y][x]
                    temp_dest = self.board_matrix[dest[1]][dest[0]]
                    self.board_matrix[y][x] = 0
                    self.board_matrix[dest[1]][dest[0]] = current_player

                    eval, _ = self.minimax(depth - 1, alpha, beta, False)
                    self.board_matrix[dest[1]][dest[0]] = temp_dest
                    self.board_matrix[y][x] = temp_origin

                    if eval < min_eval:
                        min_eval = eval
                        best_move = move

                # Prune if the current evaluation is better than the alpha value for the maximizing player
                if eval <= alpha:
                    break

            return min_eval, best_move



def main():
    print("Escolha o time que deseja jogar:")
    print("1. Silver (peças 1)")
    print("2. Gold (peças 2)")

    player_choice = int(input("Digite o número correspondente à sua escolha: "))

    if player_choice not in [1, 2]:
        print("Escolha inválida. Por favor, selecione 1 para Azul ou 2 para Verde.")
        return

    ai_player = 3 - player_choice  # Escolhe o time oposto ao do jogador
    
    

    game_instance = rawGame(player_choice, ai_player)
    game_instance.game()

if __name__ == "__main__":
    main()

