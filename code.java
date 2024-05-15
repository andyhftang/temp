import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

public class AzureBlobStorageLister {

    public static void main(String[] args) {
        String connectionString = "your-connection-string";
        String containerName = "your-container-name";

        listBlobsInContainer(connectionString, containerName);
    }

    /**
     * List all blobs in the specified container.
     *
     * @param connectionString The connection string for the Azure Blob Storage account.
     * @param containerName    The name of the container to list blobs from.
     */
    public static void listBlobsInContainer(String connectionString, String containerName) {
        // Create a BlobServiceClient using the connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        // Get the container client
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        // List all blobs in the container
        for (BlobItem blobItem : containerClient.listBlobs()) {
            System.out.println("Blob name: " + blobItem.getName());
        }
    }
}
