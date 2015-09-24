package de.fhws.hablame.chatbotbackend.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * The superclass of the model classes.
 * This class is mapped with {@link MappedSuperClass}, so it does not get persisted by the javax.persistent orm.
 * All model classes extend this class, so we have less work with implementing things like an {@link Id} for each model.
 */
@MappedSuperclass
public abstract class MappedSuperClass implements Serializable {

	@Transient
	private static final long serialVersionUID = 7892254636225159371L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false, nullable=false, unique=true, insertable=false)
	private Long id;
	
	@Version
	@Column(name="version", nullable=false)
	private int version;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="last_update")
	private Date lastUpdate;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="create_time", nullable=false)
	private Date createTime;
	
	@Column(name="active", nullable=false)
	private Boolean active;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
