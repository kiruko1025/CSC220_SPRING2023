package prog11;

import prog05.ArrayQueue;
import prog08.ExternalSort;
import prog08.TestExternalSort;
import prog09.BTree;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class NotGPT implements SearchEngine {

    HardDisk pageDisk = new HardDisk();

    HardDisk wordDisk = new HardDisk();

    Map<String, Long> indexOfWord = new HashMap<String, Long>();
    Map<String, String> indexOfURL = new prog09.BTree(100);

    class Vote implements Comparable<Vote> {
        long index;
        double impact;

        @Override
        public int compareTo(Vote o) {
            if (!(index == o.index)) {
                return (int) (index - o.index);
            } else {
                return Double.compare(impact, o.impact);
            }
        }

        public String toString() {
            return index + " " + impact;
        }
    }

    class VoteScanner implements ExternalSort.EScanner<Vote> {
        class Iter implements Iterator<Vote> {
            Scanner in;

            Iter(String fileName) {
                try {
                    in = new Scanner(new File(fileName));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            public boolean hasNext() {
                return in.hasNext();
            }

            public Vote next() {
                Vote nextVote = new Vote();
                nextVote.index = in.nextLong();
                nextVote.impact = in.nextDouble();
                return nextVote;
            }
        }

        public Iterator<Vote> iterator(String fileName) {
            return new VoteScanner.Iter(fileName);
        }
    }

    class PageIndexComparator implements Comparator<Long> {
        public int compare(Long index1, Long index2) {
            InfoFile file1 = pageDisk.get(index1);
            InfoFile file2 = pageDisk.get(index2);
            return Double.compare(file1.impact, file2.impact);
        }
    }
    public long indexPage(String url) {
        long index = pageDisk.newFile();
        InfoFile file = new InfoFile(url);
        pageDisk.put(index, file);
        indexOfURL.put(url, Long.toString(index));
        System.out.println("Indexing url " + url + " at " + index + " file " + file);
        return index;
    }

    public long indexWord(String word) {
        long index = wordDisk.newFile();
        InfoFile file = new InfoFile(word);
        wordDisk.put(index, file);
        indexOfWord.put(word, index);
        return index;
    }

    @Override
    public void collect(Browser browser, List<String> startingURLs) {

        Queue<Long> pageIndices = new ArrayQueue<Long>();

        for (String url : startingURLs) {

            if (indexOfURL.containsKey(url)) {
                continue;
            }
            long index = indexPage(url);
            pageIndices.offer(index);
        }
        while (!pageIndices.isEmpty()) {
            long index = pageIndices.poll();
            Boolean load = browser.loadPage(pageDisk.get(index).data);
            if (!load)
                continue;
            List<String> urls = browser.getURLs();
            List<String> words = browser.getWords();
            Set<String> visitedurls = new HashSet<String>();
            for (String url : urls) {
                long newIndex = 0;
                if (!indexOfURL.containsKey(url)) {
                    newIndex = indexPage(url);
                    pageIndices.offer(newIndex);
                } else {
                    newIndex = Long.parseLong(indexOfURL.get(url));
                }

                if (!visitedurls.contains(url)) {
                    visitedurls.add(url);
                    pageDisk.get(index).indices.add(newIndex);
                }
            }
            for (String word : words) {
                long wordIndex = 0;
                if (!indexOfWord.containsKey(word)) {
                    wordIndex = indexWord(word);
                } else {
                    wordIndex = indexOfWord.get(word);
                }
                if (!wordDisk.get(wordIndex).indices.contains(index)) {
                    wordDisk.get(wordIndex).indices.add(index);
                }
            }


        }

    }

    @Override
    public void rank(boolean fast) {
        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.impact = 1.0;
            file.impactTemp = 0.0;
        }
        if (fast) {

            for (int i = 0; i < 20; i++) {
                rankFast();
            }

        } else {
            for (int i = 0; i < 20; i++) {
                rankSlow();
            }
        }


    }

    void rankSlow() {
        double zeroLinkImpact = 0.0;
        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            double impactPerIndex = file.impact / file.indices.size();
            if (file.indices.isEmpty()) {
                zeroLinkImpact += file.impact;
            }
            for (long index2 : file.indices) {
                pageDisk.get(index2).impactTemp += impactPerIndex;
            }
        }
        zeroLinkImpact /= pageDisk.size();
        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.impact = file.impactTemp;
            file.impact += zeroLinkImpact;
            file.impactTemp = 0.0;
        }

    }

    void rankFast() {
        double zeroLinkImpact = 0.0;
        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            InfoFile file = entry.getValue();
            if (file.indices.isEmpty()) {
                zeroLinkImpact += file.impact;
            }
        }
        zeroLinkImpact /= pageDisk.size();
        try {
            PrintWriter out = new PrintWriter("unsorted-votes.txt");
            for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
                InfoFile file = entry.getValue();
                Vote vote = new Vote();
                for (long index2 : file.indices) {
                    vote.index = index2;
                    vote.impact = file.impact / file.indices.size();

                    out.println(vote);
                }
            }
            out.close();
            VoteScanner scanner = new VoteScanner();
            ExternalSort sorter = new ExternalSort<Vote>(scanner);
            sorter.sort("unsorted-votes.txt", "sorted-votes.txt");
        } catch (Exception e) {
            System.out.println(e);
        }

        VoteScanner scanner = new VoteScanner();
        Iterator<Vote> votes = scanner.iterator("sorted-votes.txt");
        Vote vote = null;
        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            Long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.impact = 0.0;
            file.impact += zeroLinkImpact;

            if (vote == null && votes.hasNext()) {
                vote = votes.next();
            }
            while (vote != null && vote.index == index) {
                file.impact += vote.impact;
                if (votes.hasNext()) {
                    vote = votes.next();
                } else {
                    vote = null;
                }


            }

        }


    }

    @Override
    public String[] search(List<String> searchWords, int numResults) {
        Iterator<Long>[] pageIndexIterators =
                (Iterator<Long>[]) new Iterator[searchWords.size()];
        long[] currentPageIndices;
        currentPageIndices = new long[searchWords.size()];
        PageIndexComparator comparator = new PageIndexComparator();
        PriorityQueue<Long> bestPageIndices = new PriorityQueue<Long>(numResults, new PageIndexComparator());
        //Write a loop to initialize the entries of pageIndexIterators.
        //   pageIndexIterators[i] should be set to an iterator over the page
        //   indices in the file of searchWords[i].

        for (int i = 0; i < searchWords.size(); i++) {
            long wordIndex = indexOfWord.get(searchWords.get(i));
            pageIndexIterators[i] = wordDisk.get(wordIndex).indices.iterator();
        }
        while (getNextPageIndices(currentPageIndices, pageIndexIterators)) {
            if (allEqual(currentPageIndices)) {
                System.out.println(pageDisk.get(currentPageIndices[0]).data + "impact" + pageDisk.get(currentPageIndices[0]).impact);
                if (bestPageIndices.size() < numResults) {
                    bestPageIndices.offer(currentPageIndices[0]);
                } else {
                    if (comparator.compare(currentPageIndices[0], bestPageIndices.peek()) > 0) {
                        bestPageIndices.poll();
                        bestPageIndices.offer(currentPageIndices[0]);
                    }
                }
            }
        }
        if(numResults>bestPageIndices.size()){
            numResults = bestPageIndices.size();
        }
        String[] result = new String[numResults];
        Stack<Long> stack = new Stack<Long>();
        while (!bestPageIndices.isEmpty()) {
            stack.push(bestPageIndices.poll());
        }
        for(int i=0;!stack.isEmpty(); i++){
            result[i] = pageDisk.get(stack.pop()).data;
        }
        return result;
    }

    private boolean allEqual(long[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] != array[0]) {
                return false;
            }
        }
        return true;
    }

    private long getLargest(long[] array) {
        long largest = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > largest) {
                largest = array[i];
            }
        }
        return largest;
    }

    private boolean getNextPageIndices
            (long[] currentPageIndices, Iterator<Long>[] pageIndexIterators) {
        if (allEqual(currentPageIndices)) {
            for (int i = 0; i < currentPageIndices.length; i++) {
                if (!pageIndexIterators[i].hasNext()) {
                    return false;
                }
                currentPageIndices[i] = pageIndexIterators[i].next();
            }

        }else {
            for (int i = 0; i < currentPageIndices.length; i++) {
                if (currentPageIndices[i] < getLargest(currentPageIndices)) {
                    if (!pageIndexIterators[i].hasNext()) {
                        return false;
                    }
                    currentPageIndices[i] = pageIndexIterators[i].next();
                }
            }

        }
        return true;



    }
}
