package onboarding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Problem7 {

    private static final int BOTH_KNOW_ONE_FRIEND_SCORE = 10;
    private static final int ANSWER_SIZE_MAX = 5;

    public static List<String> solution(String user, List<List<String>> friends, List<String> visitors) {
        Map<String, List<String>> userToFriends = parseFriends(friends);
        Map<String, Integer> bothKnowFriendsScore = computeBothKnowFriendsScore(user, userToFriends);
        Map<String, Integer> visitScore = computeVisitScore(visitors);
        Map<String, Integer> recommendScore = computeRecommendScore(bothKnowFriendsScore, visitScore);

        return computeAnswer(user, userToFriends.get(user), recommendScore);
    }

    static List<String> computeAnswer(String mainCharacter, List<String> mainCharacterFriends, Map<String, Integer> recommendScore) {
        List<Map.Entry<String, Integer>> recommendScoreList = new ArrayList<>(recommendScore.entrySet());

        recommendScoreList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return recommendScoreList.stream()
            .filter(entry -> entry.getKey() != mainCharacter)
            .filter(entry -> !mainCharacterFriends.contains(entry.getKey()))
            .filter(entry -> entry.getValue() > 0)
            .map(entry -> entry.getKey())
            .limit(ANSWER_SIZE_MAX)
            .collect(Collectors.toList());
    }

    static Map<String, Integer> computeRecommendScore(Map<String, Integer> bothKnowFriendsScore, Map<String, Integer> visitScore) {
        Map<String, Integer> recommendScore = new HashMap<>(bothKnowFriendsScore);

        visitScore.forEach((user, score) -> {
            if (!recommendScore.containsKey(user)) {
                recommendScore.put(user, 0);
            }
            recommendScore.put(user, recommendScore.get(user) + score);
        });
        return recommendScore;
    }

    static Map<String, Integer> computeVisitScore(List<String> visitors) {
        Map<String, Integer> visitCount = visitors.stream()
            .distinct()
            .collect(Collectors.toMap(visitor -> visitor, visitor -> 0));

        visitors.forEach(visitor -> visitCount.put(visitor, visitCount.get(visitor) + 1));
        return visitCount;
    }

    static Map<String, Integer> computeBothKnowFriendsScore(String mainCharacter, Map<String, List<String>> userToFriends) {
        Map<String, Integer> bothKnowFriendsScore = new HashMap<>();
        List<String> mainCharacterFriends = userToFriends.get(mainCharacter);

        userToFriends.forEach((user, friends) -> bothKnowFriendsScore.put(user, BOTH_KNOW_ONE_FRIEND_SCORE * countBothKnowFriends(mainCharacterFriends, friends)));
        return bothKnowFriendsScore;
    }

    private static int countBothKnowFriends(List<String> friends1, List<String> friends2) {
        return (int) friends1.stream()
            .filter(friend1 -> friends2.contains(friend1))
            .count();
    }

    //    static List<String> findAllUserWithoutMainCharacter(String mainCharacter, Map<String, List<String>> userToFriends, List<String> visitors) {
    //        List<String> allUserWithoutMainCharacter = new ArrayList<>();
    //
    //        userToFriends.forEach((user, friends) -> allUserWithoutMainCharacter.add(user));
    //        visitors.forEach(visitor -> allUserWithoutMainCharacter.add(visitor));
    //        return allUserWithoutMainCharacter.stream()
    //            .filter(user -> !user.equals(mainCharacter))
    //            .distinct()
    //            .collect(Collectors.toList());
    //    }

    //    static List<String> exceptAlreadyFriends(List<String> allFriends, List<String> alreadyFriends) {
    //        return allFriends.stream()
    //            .filter(friend -> !alreadyFriends.contains(friend))
    //            .collect(Collectors.toList());
    //    }

    static Map<String, List<String>> parseFriends(List<List<String>> friends) {
        Map<String, List<String>> parsedFriends = new HashMap<>();

        friends.forEach(user -> {
            String user1 = user.get(0);
            String user2 = user.get(1);

            addFriend(parsedFriends, user1, user2);
            addFriend(parsedFriends, user2, user1);
        });

        return parsedFriends;
    }

    private static void addFriend(Map<String, List<String>> parsedFriends, String user1, String user2) {
        if (!parsedFriends.containsKey(user1)) {
            parsedFriends.put(user1, new ArrayList<>());
        }
        List<String> userFriends = parsedFriends.get(user1);
        userFriends.add(user2);
        parsedFriends.put(user1, userFriends);
    }

}
