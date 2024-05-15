import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;

public class S3RoleAssumer {

    public static void main(String[] args) {
        String roleArn = "arn:aws:iam::123456789012:role/example-role";
        String bucketName = "example-bucket";
        listS3BucketObjects(roleArn, bucketName);
    }

    /**
     * Method to assume a role and list objects in the specified S3 bucket.
     *
     * @param roleArn    The ARN of the role to be assumed.
     * @param bucketName The name of the S3 bucket.
     */
    public static void listS3BucketObjects(String roleArn, String bucketName) {
        Region region = Region.US_EAST_1; // Specify your region

        // Create an STS client
        StsClient stsClient = StsClient.builder()
                                       .region(region)
                                       .build();

        // Assume the role
        AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                                                         .roleArn(roleArn)
                                                         .roleSessionName("S3RoleSession")
                                                         .build();

        AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);

        AwsBasicCredentials temporaryCredentials = AwsBasicCredentials.create(
                roleResponse.credentials().accessKeyId(),
                roleResponse.credentials().secretAccessKey(),
                roleResponse.credentials().sessionToken()
        );

        StsAssumeRoleCredentialsProvider credentialsProvider = StsAssumeRoleCredentialsProvider.builder()
                .stsClient(stsClient)
                .refreshRequest(roleRequest)
                .build();

        // Create an S3 client with the assumed role credentials
        S3Client s3Client = S3Client.builder()
                                    .region(region)
                                    .credentialsProvider(credentialsProvider)
                                    .build();

        // List objects in the specified S3 bucket
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                                                                      .bucket(bucketName)
                                                                      .build();

        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

        for (S3Object s3Object : listObjectsResponse.contents()) {
            System.out.println(" - " + s3Object.key());
        }

        // Close the S3 client
        s3Client.close();
    }
}
