# Polyominoes
This a final research project designed to test my knowledge of discrete mathematics, specifically challenging my knowledge of Graph Theory, Set Theory, and Recursion. As the description states, given some game of checkers, the program must find all the polyominoes enclosed. A polyomino is a plane geometric figure formed by joining one or more equal squares edge to edge. It is a polyform whose cells are squares. It may be regarded as a finite subset of the regular square tiling. 

![image](https://github.com/user-attachments/assets/fed8a67a-c647-4bc4-9369-ecb25f4f920f)

It must change on every turn, so it must be efficient enough not to slow down the game or cause significant lag between the polyomino view and the game view.

Most challengingly, it must also be accurate. This means an adequate knowledge of Graph and Set Theory must be used to boil the checkerboard down from a game into a set of vertices and edges. Then, utilizing an adjacency algorithm I developed which recursively checks connections between sets of vertices and edges for commonalities, groups of connected vertices are found and then displayed dynamically on the checkerboard.

<img width="438" alt="image" src="https://github.com/user-attachments/assets/104d9465-90c3-4ff7-b3d2-9a40889180c6">

The actual game is on the left, while the polyomino preview is magnified and on the right side, as it's the important part of the project.

The flowchart of the aforementioned algorithm is documented below.

![image](https://github.com/user-attachments/assets/b2a15402-7337-4394-a6b1-e51f9db1841e)
