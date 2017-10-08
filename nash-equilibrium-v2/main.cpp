#include <iostream>
#include <vector>
#include <limits>

struct NashBoard {
    int board[4][4][2] = {
            {{4, 2}, {0, 0}, {5, 0},  {0,  0}},
            {{1, 4}, {1, 4}, {0, 5},  {-1, 0}},
            {{0, 0}, {2, 4}, {1, 2},  {0,  0}},
            {{0, 0}, {0, 0}, {0, -1}, {0,  0}}
    };
};

void printBoard(const NashBoard &nashBoard) {
    for (int i = 0; i < 4; ++i) {
        for (int j = 0; j < 4; ++j) {
            std::cout << '(';
            for (int k = 0; k < 2; ++k) {
                if (k != 1) std::cout << nashBoard.board[i][j][k] << ", ";
                else std::cout << nashBoard.board[i][j][k];
            }
            std::cout << ") ";
        }
        std::cout << '\n';
    }
    std::cout << "==========================\n";
}

void findNashEquilibrium(const NashBoard &b) {
    std::vector<int> b_vector(4), a_vector(4); // each vector holds 4 elements
    int b_max = std::numeric_limits<int>::lowest(); // negative infinity for b - rows
    int a_max = std::numeric_limits<int>::lowest(); // negative infinity for a - columns

    for (int i = 0; i < 4; ++i) {
        for (int j = 0; j < 4; ++j) {
            // operate on B
            if (b.board[i][j][1] > b_max) {
                b_vector[i] = b.board[i][j][1]; // insert at index. each row is another index
                b_max = b.board[i][j][1];
            }
            // now operate on A
            if (b.board[j][i][0] > a_max) {
                a_vector[i] = b.board[j][i][0]; // insert at index. each col is another index
                a_max = b.board[j][i][0];
            }
        }
        b_max = std::numeric_limits<int>::lowest(); // reset b_max for next row
        a_max = std::numeric_limits<int>::lowest(); // reset a_max for next col
    }

    for (const auto &x : b_vector) std::cout << x << ' ';
    std::cout << " - B\n";
    for (const auto &x : a_vector) std::cout << x << ' ';
    std::cout << " - A\n";

    int found = 0;
    for (int i = 0; i < 4; ++i) {
        for (int j = 0; j < 4; ++j) {
            if (b.board[i][j][1] == b_vector[i] && b.board[i][j][0] == a_vector[j]) {
                std::cout << "Nash equilibrium found at: ("
                          << i << ", " << j << ") => Value: ["
                          << b.board[i][j][0] << ", "
                          << b.board[i][j][1] << "]\n";
                ++found;
            }
        }
    }
    std::cout << "Found " << found << " Nash equilibria\n";
}

int main() {
    NashBoard b;
    printBoard(b);
    findNashEquilibrium(b);
}