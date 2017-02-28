package mapreduce.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class WordCount extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new WordCount(), args));
    }

    public int run(String[] args) throws Exception {
        Configuration configuration = getConf();
        Job job = Job.getInstance(configuration, "Word Count");

        Path inputPath = new Path("/bdhs/data/books");
        Path outputPath = new Path("/bdhs/data-output/wordcount/mapreduce");

        FileSystem fs = FileSystem.get(configuration);
        if(fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        job.setJarByClass(WordCount.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        TextInputFormat.addInputPath(job, inputPath);
        TextOutputFormat.setOutputPath(job, outputPath);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        Boolean verbose = false;
        if(args != null && args.length > 0 && args[args.length - 1].toLowerCase() == "verbose") {
            verbose = true;
        }
        return job.waitForCompletion(verbose) ? 0 : 1;
    }

    public static class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        private final String[] stopwords = new String[] {"a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"};
        private final LongWritable one = new LongWritable(1);

        @Override
        protected void map(LongWritable bytesOffset, Text line, Context context) throws IOException, InterruptedException {
            String[] lineSplit = line.toString().toLowerCase().split("[^a-zA-Z0-9]");
            Text word = new Text();
            for (String s : lineSplit) {
                if(!isStopWord(s)) {
                    word.set(s);
                    context.write(word, one);
                }
            }
        }

        private boolean isStopWord(String s) {
            for (String stopword : stopwords) {
                if(s.equalsIgnoreCase(stopword)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        protected void reduce(Text word, Iterable<LongWritable> counts, Context context) throws IOException, InterruptedException {
            Long sum = 0L;
            for (LongWritable count : counts){
                sum += count.get();
            }
            context.write(word, new LongWritable(sum));
        }
    }
}
