/**
 * CS 4491 Intelligent Computing & Logical Deduction
 *
 * This program is strictly made to demonstrate solving
 * nash equilibrium. Works only on 4x4x2 Nashboard.
 * Future plans to allow user to enter values for their own nashboard.
 */

#include <vector>
#include <iostream>
#include <algorithm>
#include <unordered_map>

typedef std::pair<std::pair<int, int>, int> nash_pair;

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

struct SimpleHash {
    std::size_t operator()(const std::pair<int, int> &p) const {
        return static_cast<size_t>(p.first ^ p.second);
    }
};

void findNashEquilibrium(const NashBoard &nashBoard) {

    // SECTION B
    std::unordered_map<std::pair<int, int>, int, SimpleHash> umap_buffer;
    std::unordered_map<std::pair<int, int>, int, SimpleHash> b_map;
    for (int i = 0; i < 4; ++i) {
        for (int j = 0; j < 4; ++j) {
            for (int k = 0; k < 2; ++k) {
                if (k == 1) {
                    umap_buffer.emplace(std::make_pair(i, j), nashBoard.board[i][j][k]);
                }
            }
        }
        auto something = std::max_element(umap_buffer.begin(), umap_buffer.end(),
                                          [=](const nash_pair &npr1, const nash_pair &npr2) {
                                              return npr1.second < npr2.second;
                                          });
        b_map.insert(*something);
        std::cout << something->first.first << ',' << something->first.second << " => " << something->second << '\n';
        umap_buffer.clear();
    }
    std::cout << "Entity B (top/right)\n==========================\n";

    // SECTION A
    std::unordered_map<std::pair<int, int>, int, SimpleHash> a_map;
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; ++j) {
            umap_buffer.emplace(std::make_pair(j, i), nashBoard.board[j][i][0]);    // 0 because we want first element
        }
        auto something = std::max_element(umap_buffer.begin(), umap_buffer.end(),
                                          [=](const nash_pair &npr1, const nash_pair &npr2) {
                                              return npr1.second < npr2.second;
                                          });
        a_map.insert(*something);
        std::cout << something->first.first << ',' << something->first.second << " => " << something->second << '\n';
        umap_buffer.clear();
    }
    std::cout << "Entity A (side/left)\n==========================\n";

    // CLEANUP
    std::unordered_map<std::pair<int, int>, int, SimpleHash> nash_map;
    for (const auto &x : a_map) {
        auto pair = b_map.insert(x);  // returns pair<iterator, bool>
        if (!pair.second) {
            // std::cout << "2D coordinate (" << pair.first->first.first << ',' << pair.first->first.second << ") CONTAINS nash pair\n";
            nash_map.insert(*pair.first); // pair.first is iterator to pair, so we dereference it
        }
    }

    int tmp = 0;
    for (const auto &x : nash_map) {
        std::cout << "Nash Coordinates are (" << x.first.first << ',' << x.first.second << ") Values => [";
        std::cout << nashBoard.board[x.first.first][x.first.second][0] << ", ";
        std::cout << nashBoard.board[x.first.first][x.first.second][1] << "]\n";
        ++tmp;
    }
    std::cout << "Total " << tmp << " Nash Equilibria\n";

}   // end findNashEquilibrium

int main() {
    NashBoard nashBoard = NashBoard();
    printBoard(nashBoard);
    findNashEquilibrium(nashBoard);
    return 0;
}