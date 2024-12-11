#Explicacao mais detalha do codigo no relatorio, os comentarios aqui são apenas para se guiar

class rawGame:
    def __init__(self, player_choice, ai_player): #Funcao de inicializacao, setando o tabuleiro e algumas variaveis auxiliares
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
            self.board_matrix[col][row] = 3  # Define a célula como 3 para representar a bandeira

        self.selected_piece = None
        self.selected_piece2 = None
        self.selected_flag = None
        self.player = player_choice

        self.ai_player = ai_player
        self.previous_opponent_pieces = self.numPieces2
    
    def verificar_bandeira_nos_cantos(self): #Funcao para verificar se a bandeira chegou nos cantos
        # Obtém as dimensões do tabuleiro
        rows = 7
        cols = 7
        
        # Percorre todas as posições na borda do tabuleiro verificando a bandeira
        for i in range(rows):
            for j in range(cols):
                if i == 0 or i == rows - 1 or j == 0 or j == cols - 1:  # Verifica se a posição está na borda
                    if self.board_matrix[i][j] == 'B':  # Verifica se há uma bandeira na posição
                        print("A bandeira está em uma das bordas do tabuleiro!")
                        print("Jogo acabou")
                        exit(0)

    def get_player_pieces(self,player): #Funcao para pegar a quantidade de pecas do player
        return self.numPieces if player == 1 else self.numPieces2
            
    def evaluate_board_killer(self, player, num): #Funcao heuristica focada em ganhar tempo e matar a bandeira
        weight_pieces = 10.0
        player_pieces = self.numPieces if player == 1 else self.numPieces2
        opponent_pieces = self.numPieces2 if player == 1 else self.numPieces
        x, y = self.find_flag_position()

        score = weight_pieces * player_pieces
        
        if self.is_piece_dead(num):
            return 1000

        if self.is_flag_threatened(x, y):
            return 1000

        if self.is_flag_dead():
            return 100000
        

        # Deduz pontos quando as peças do oponente são capturadas
        opponent_score = weight_pieces * opponent_pieces
        score -= opponent_score

        return score

        
            
    def evaluate_board_agressive(self, player): #Funcao heuristica focada em matar o maior numero de pecas do oponente
        weight_pieces = 1.0 
        weight_capture = 10.0  
        weight_threat = 10000.0  

        player_pieces = self.numPieces if player == 1 else self.numPieces2
        opponent_pieces = self.numPieces2 if player == 1 else self.numPieces

        score = weight_pieces * player_pieces


        score -= opponent_pieces * weight_capture

        if self.are_pieces_threatened(player):
            score -= weight_threat  # Penalize heavily for threatened pieces

        return score


            
    def evaluate_board_defensive(self, player): #Funcao defensiva focada em defender as pecas e fugir com a bandeira

        weight_pieces = 1.0
        weight_defense = 100.0
        weight_flag = 1000.0
        weight_border = 500.0

        player_pieces = self.numPieces if player == 1 else self.numPieces2
        opponent_pieces = self.numPieces2 if player == 1 else self.numPieces

        score = weight_pieces * player_pieces

        x, y = self.find_flag_position()

        # Distância da bandeira à borda
        distance_to_border = min(x, y, self.board_size - 1 - x, self.board_size - 1 - y)
        border_score = weight_border * (self.board_size - distance_to_border)
        
        if distance_to_border == 0:
            return score * 10000

        # Peças ameaçadas
        pieces_threatened = self.are_pieces_threatened(player)

        if pieces_threatened:
            score += weight_defense

        if self.is_flag_threatened(x, y):
            #print("TA AMEACADA PORRA!!!")
            score -= weight_flag
        return score - weight_pieces * opponent_pieces + border_score



    def are_pieces_threatened(self, player): #Funcao para verificar se tem alguma peca sendo ameacada
        # Verifica se alguma peça do jogador está ameaçada
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self.board_matrix[row][col] == player:
                    for d_row in range(-1, 2):
                        for d_col in range(-1, 2):
                            if (d_row != 0 or d_col != 0) and 0 <= row + d_row < self.board_size and 0 <= col + d_col < self.board_size:
                                if self.board_matrix[row + d_row][col + d_col] == 1:
                                    return True
        return False
    
    def is_piece_dead(self, num): #Funcao para verificar se alguma peca foi morta
        pieces = 0
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self.board_matrix[row][col] == 2:
                    pieces += 1
                    print(pieces)
            return True if pieces != num else False


    def find_flag_position(self): #Funcao para verificar a psicao da bandeira
        # Encontra a posição da bandeira no tabuleiro
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self.board_matrix[row][col] == 3:  # Flag
                    return row, col
        return -1, -1
        
    def is_flag_threatened(self, flag_row, flag_col): #Funcao para verificar se a bandeira está ameacada
        # Verifica se a bandeira está ameaçada por peças inimigas nas diagonais
        diagonals = [(-1, -1), (-1, 1), (1, -1), (1, 1)]
        for d_row, d_col in diagonals:
            new_row = flag_row + d_row
            new_col = flag_col + d_col
            if 0 <= new_row < self.board_size and 0 <= new_col < self.board_size:
                if self.board_matrix[new_row][new_col] == 1:  # Peça inimiga
                    return True
        return False
            
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
    
    def generate_moves(self, player): #Gera uma lista com todos os movimentos possiveis para um jogador
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
                                if self.is_valid_move((row, col), dest, player) and not self.is_flag_threatened(dest[1], dest[0]):
                                    moves.append((row, col, dest))
        return moves
    
    def is_flag_dead(self): #Verifica se a bandeira morreu
        for row in range(self.board_size):
            for col in range(self.board_size):
                if self.board_matrix[row][col] == 3:
                    return False
                
        return True


    def game(self): #Funcao que gere a lógica do jogo
        current_player = self.player
        flag = False
        if self.player == 2:
            flag = True
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
            if self.verificar_bandeira_nos_cantos():
                print("bandeira fugiu")
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
                        elif self.board_matrix[y][x] == current_player + 1 and current_player == 2:
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


                if self.board_matrix[y_dest][x_dest] == other_player:
                    if other_player == 1:
                        self.numPieces -= 1
                    else:
                        self.numPieces2 -= 1
                
                if self.board_matrix[y][x] == 3:
                    
                    self.board_matrix[y_dest][x_dest] = 3
                else:
                    self.board_matrix[y_dest][x_dest] = current_player
                    
                self.board_matrix[y][x] = 0

            else:  # Caso contrário, é a vez da IA
                aux = True if self.ai_player == 2 else False
                _, best_move = self.minimax(2, alpha,beta, current_player)
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

            if self.verificar_bandeira_nos_cantos(): 
                print("bandeira fugiu")
                break
            current_player = 2 if current_player == 1 else 1  # Alterna para o próximo jogador
            other_player = 1 if current_player == 2 else 2
            self.board_matrix[y][x] = 0


    def minimax(self, depth, alpha, beta, maximizing_player): #Funcao heuristica com poda alpha-beta
        current_player = self.ai_player
        player = self.player
        moves = self.generate_moves(current_player)
        flag = self.find_flag_position()
        
        if depth == 0:
            return self.evaluate_board_defensive(maximizing_player), None  # Use evaluate_moves at leaf nodes

        if maximizing_player:
            max_eval = float('-inf')
            best_move = None
            for move in moves:
                x, y, dest = move
                if self.board_matrix[y][x] == current_player or 3:
                    temp_origin = self.board_matrix[y][x]
                    temp_dest = self.board_matrix[dest[1]][dest[0]]
                    self.board_matrix[y][x] = 0
                    self.board_matrix[dest[1]][dest[0]] = current_player
                    eval, _ = self.minimax(depth - 1, alpha, beta, False)
                    self.board_matrix[dest[1]][dest[0]] = temp_dest
                    self.board_matrix[y][x] = temp_origin
                    if eval > max_eval:
                        max_eval = eval
                        best_move = move
                    alpha = max(alpha, eval)
                    if beta <= alpha:
                        break
                            
            return max_eval, best_move
        else:
            min_eval = float('inf')
            best_move = None
            for move in moves:
                x, y, dest = move
                if self.board_matrix[y][x] == current_player:
                    temp_origin = self.board_matrix[y][x]
                    temp_dest = self.board_matrix[dest[1]][dest[0]]
                    self.board_matrix[y][x] = 0
                    self.board_matrix[dest[1]][dest[0]] = current_player
                    eval, _ = self.minimax(depth - 1, alpha, beta, True)
                    self.board_matrix[dest[1]][dest[0]] = temp_dest
                    self.board_matrix[y][x] = temp_origin
                    if eval < min_eval:
                        min_eval = eval
                        best_move = move
                    beta = min(beta, eval)
                    if beta <= alpha:
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

    ai_player = 3 - player_choice

    game_instance = rawGame(player_choice, ai_player)
    game_instance.game()

if __name__ == "__main__":
    main()
