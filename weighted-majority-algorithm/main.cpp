#include <iostream>
#include <random>
#include <chrono>
#include <algorithm>

struct Experts {
    double _weight;
    int _mistakes;
    int _decision;
    bool _correct;
    bool _majority;

    explicit Experts(double p_weight = 0.5) : _weight(p_weight), _mistakes(), _decision(), _correct(), _majority() {}
};

int main() {
    const int numExperts = 8;
    const int numTrials = 20;
    constexpr float betaConstant = 0.7355;
    auto seed = std::chrono::system_clock::now().time_since_epoch().count();
    std::mt19937_64 mt(seed);
    std::uniform_real_distribution<double> uniform_real_distribution(0, 1);  // [0 - 1]

    std::vector<Experts> vec;
    for (int i = 0; i < numExperts; ++i) {
        vec.emplace_back(1);    // or use param: uniform_real_distribution(mt) to simulate real-life bias.
    }

    std::cout << "Initial Values for Each Expert\n";
    for (int i = 0; i < static_cast<int>(vec.size()); ++i) {
        std::cout << "Expert: " << i
                  << " Mistakes: " << vec[i]._mistakes
                  << " Weight: " << vec[i]._weight
                  << '\n';
    }   std::cout << "-----------------------------\n";

     /*
     Experts vote. We tally weights of those that voted 0 and those that voted 1.
     The side with the highest combined weight is the weighted-majority, else minority.
     */

    for (int k = 0; k < numTrials; ++k) {

        // Each expert makes a decision...
        std::uniform_int_distribution<int> uniform_int_distribution(0, 1);  // 0 or 1
        int label = uniform_int_distribution(mt);
        int num1s = 0, num0s = 0;
        for (int i = 0; i < numExperts; ++i) {
            vec[i]._decision = uniform_int_distribution(mt);
            vec[i]._decision == 1 ? num1s++ : num0s++;
            vec[i]._correct = vec[i]._decision == label;
            if (vec[i]._decision != label) vec[i]._mistakes += 1;
        }

        // Determine 0 or 1 majority based on accumulated weights for each choice aka weighted-majority
        double sumWt0 = 0, sumWt1 = 0;
        std::for_each(vec.begin(), vec.end(), [=](const Experts& x) mutable {
            x._decision == 0 ? sumWt0 += x._weight : sumWt1 += x._weight;
        });
        int majority = (std::max(sumWt0, sumWt1) == sumWt0) ? 0 : 1;  // if max weight is sum0 my decision is 0, else 1
        for (Experts &x : vec) {
            x._majority = x._decision == majority;
        }

        // If my decision is correct, multiply minority experts by beta
        // If my decision is wrong, multiply majority experts by beta
        int myDecision = majority;
        bool myCorrect = myDecision == label;
        if (myCorrect) {
            for (Experts &x : vec) {
                if (!x._majority) x._weight *= betaConstant;
            }
        }
        else {
            for (Experts &x : vec) {
                if (x._majority) x._weight *= betaConstant;
            }
        }

        // Print results per trial
        int tmpn1 = 0;
        std::cout << "Label: " << label << '\n';
        for (const Experts &x : vec) {
            std::cout
                    << "Expert: " << tmpn1++
                    << " Decision: " << x._decision
                    << " Correct: " << x._correct
                    << " Mistakes: " << x._mistakes
                    << " Weight: " << x._weight << '\n';
        }   std::cout << "-----------------------------\n";

    }   // end numTrials for-loop

    for (int i = 0; i < static_cast<int>(vec.size()); ++i) {
        std::cout << "Expert: " << i << " Total_Mistakes: " << vec[i]._mistakes << " Weight: " << vec[i]._weight << '\n';
    }

}   // end main()
