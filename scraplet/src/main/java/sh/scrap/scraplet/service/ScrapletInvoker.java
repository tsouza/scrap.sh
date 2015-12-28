package sh.scrap.scraplet.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public interface ScrapletInvoker {

    Result invoke(Map<String, Object> metadata, Object data) throws Exception;

    class Result {

        @JsonProperty("ReceivedBytes")
        private final int receivedBytes;

        @JsonProperty("ProcessedBytes")
        private final int processedBytes;

        @JsonProperty("Output")
        private final Map<String, Object> output;

        public Result(int receivedBytes, int processedBytes, Map<String, Object> output) {
            this.receivedBytes = receivedBytes;
            this.processedBytes = processedBytes;
            this.output = output;
        }

        public int getReceivedBytes() {
            return receivedBytes;
        }

        public int getProcessedBytes() {
            return processedBytes;
        }

        public Map<String, Object> getOutput() {
            return output;
        }
    }
}
