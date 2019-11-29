package org.jazzcommunity.GitConnectorService.dcc.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "commit")
public class Commit {

  @Expose
  @SerializedName("c")
  @XmlCDATA
  private String comment;

  // this really needs to be a datetime data type, but somehow, parsing seems to fail
  // when using gson on the naked date payload.
  @Expose
  @SerializedName("d")
  private String date;

  @Expose
  @SerializedName("e")
  private String commiterEmail;

  @Expose
  @SerializedName("k")
  private String repositoryKey;

  @Expose
  @SerializedName("n")
  private String commiterName;

  @Expose
  @SerializedName("s")
  private String sha;

  @Expose
  @SerializedName("u")
  private String commitUrl;

  private String linkedFrom;

  private String linkUrl;

  private String projectArea;

  public String getComment() {
    return comment;
  }

  public String getDate() {
    return date;
  }

  public String getCommiterEmail() {
    return commiterEmail;
  }

  public String getRepositoryKey() {
    return repositoryKey;
  }

  public String getCommiterName() {
    return commiterName;
  }

  public String getSha() {
    return sha;
  }

  public String getCommitUrl() {
    return commitUrl;
  }

  public String getLinkedFrom() {
    return linkedFrom;
  }

  public void setLinkedFrom(String parent) {
    this.linkedFrom = parent;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public void setShortSha(String sha) {
    // explicitly does nothing because this should only be a generated property. however, jaxb
    // expects there to be both a getter and a setter for an xml property, and will not create the
    // attribute in xml otherwise.
  }

  public String getShortSha() {
    return sha.substring(0, 7);
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setCommiterEmail(String commiterEmail) {
    this.commiterEmail = commiterEmail;
  }

  public void setRepositoryKey(String repositoryKey) {
    this.repositoryKey = repositoryKey;
  }

  public void setCommiterName(String commiterName) {
    this.commiterName = commiterName;
  }

  public void setSha(String sha) {
    this.sha = sha;
  }

  public void setCommitUrl(String commitUrl) {
    this.commitUrl = commitUrl;
  }

  @Override
  public String toString() {
    return "Commit{\n"
        + "\tcomment='"
        + comment
        + "\',\n\tdate="
        + date
        + "\',\n\tcommiterEmail='"
        + commiterEmail
        + "\',\n\trepositoryKey='"
        + repositoryKey
        + "\',\n\tcommiterName='"
        + commiterName
        + "\',\n\tsha='"
        + sha
        + "\',\n\turl='"
        + commitUrl
        + "\',\n\tlink='"
        + linkUrl
        + "\',\n}";
  }

  public void setProjectArea(String projectArea) {
    this.projectArea = projectArea;
  }

  public String getProjectArea() {
    return projectArea;
  }
}
