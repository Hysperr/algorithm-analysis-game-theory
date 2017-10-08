#include <iostream>
#include <array>
#include <vector>
#include <unordered_map>
#include <algorithm>


bool myfn(const std::pair<std::string, int> &p1, const std::pair<std::string, int> &p2) {
    return p1.second < p2.second;
};

struct functor {
    bool operator()(const std::pair<std::string, int> &p1, const std::pair<std::string,int> &p2) {
        return p1.second < p2.second;
    }
} myfct;

std::unordered_map<std::string, int> color_frequency(const std::vector<std::string> &vec) {

    std::unordered_map<std::string, int> map;

    for (auto &x : vec) {

        // Or use: auto pair = map.emplace(x, 1);
        std::pair<std::unordered_map<std::string, int>::const_iterator, bool> pair = map.emplace(x, 1);

        if (!pair.second) map.at(x) += 1;
    }

    for (auto it = map.begin(); it != map.end(); ++it) {
        std::cout << it->first << " => " << it->second << '\n';
    }

    auto min_element = std::min_element(map.begin(), map.end(), myfct);
    auto max_element = std::max_element(map.begin(), map.end(), myfct);


    for (auto &pair : map) {
        if (pair.second == min_element->second)
            std::cout << "Min elements in map: " << pair.first << " => " << pair.second << '\n';
    }

    // std::cout << "Min element: " << min_element->first << " => " << min_element->second << std::endl;
     std::cout << "Max element: " << max_element->first << " => " << max_element->second << std::endl;

    return map;
}

int main() {

    std::vector<std::string> vector;

    std::array<std::string, 13> red;
    red.fill("red");

    std::array<std::string, 6> green;
    green.fill("green");

    std::array<std::string, 17> blue;
    blue.fill("blue");

    std::array<std::string, 10> alpha;
    alpha.fill("alpha");

    std::array<std::string, 25> cyan;
    cyan.fill("cyan");

    for (auto &r : red) vector.push_back(r);
    for (auto &g : green) vector.push_back(g);
    for (auto &b : blue) vector.push_back(b);
    for (auto &a : alpha) vector.push_back(a);
    for (auto &c : cyan) vector.push_back(c);

    color_frequency(vector);


    return 0;
}
